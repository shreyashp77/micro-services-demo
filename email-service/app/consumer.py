from confluent_kafka import Consumer, KafkaException
from confluent_kafka.admin import AdminClient
from app.email_service import send_email
import os
import json
import time
import logging

# Set up logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def topic_exists(admin_client, topic_name, max_retries=30, retry_interval=2):
    """Check if a Kafka topic exists, with retries."""
    for attempt in range(1, max_retries + 1):
        try:
            metadata = admin_client.list_topics(timeout=10)
            available_topics = list(metadata.topics.keys())
            logging.info(f"Available topics: {available_topics}")
            
            if topic_name in metadata.topics:
                logging.info(f"Topic '{topic_name}' found!")
                return True
            else:
                logging.warning(f"Topic '{topic_name}' not found. Waiting... (Attempt {attempt}/{max_retries})")
                time.sleep(retry_interval)
        except Exception as e:
            logging.error(f"Error checking topic: {e}")
            time.sleep(retry_interval)
    
    logging.error(f"Topic '{topic_name}' not found after {max_retries} retries. Exiting...")
    return False

def process_message(msg):
    """Process Kafka message, extract details, and send email."""
    try:
        decoded = msg.value().decode('utf-8')
        logging.info(f"Decoded message: {decoded}")
        data = json.loads(decoded)

        if isinstance(data, str):
            data = json.loads(data)  # In case the JSON payload is a string

        email = data.get("email")
        order_id = data.get("order_id")

        if not email or not order_id:
            logging.warning("Missing email or order_id in message. Skipping...")
            return

        logging.info(f"Sending email to {email} for order {order_id}")
        send_email(
            recipient_email=email,
            order_id=order_id,
            smtp_server=os.getenv("SMTP_SERVER"),
            smtp_port=int(os.getenv("SMTP_PORT")),
            sender_email=os.getenv("EMAIL_FROM")
        )
    except json.JSONDecodeError as e:
        logging.error(f"Error decoding message JSON: {e}")
    except Exception as e:
        logging.error(f"Error processing message: {e}")

def start_consumer():
    bootstrap_servers = os.getenv('KAFKA_BOOTSTRAP_SERVERS')
    topic = os.getenv('KAFKA_TOPIC')
    
    if not bootstrap_servers or not topic:
        logging.error("Missing bootstrap servers or topic name in environment variables.")
        return

    # Create AdminClient to check topic existence
    admin_client = AdminClient({'bootstrap.servers': bootstrap_servers})

    # Keep checking for the topic's existence indefinitely, until the topic is available
    while True:
        if topic_exists(admin_client, topic):
            break
        else:
            logging.info(f"Topic '{topic}' is not available yet. Re-checking in 2 seconds...")

    consumer = Consumer({
        'bootstrap.servers': bootstrap_servers,
        'group.id': 'email-sender-group',
        'auto.offset.reset': 'earliest'
    })

    consumer.subscribe([topic])
    logging.info(f"Subscribed to Kafka topic: {topic}")

    try:
        while True:
            msg = consumer.poll(1.0)  # Polling with a timeout of 1 second
            
            if msg is None:
                continue  # No message, just keep polling
            
            if msg.error():
                raise KafkaException(msg.error())  # Raise Kafka exceptions

            # Process the message
            process_message(msg)

    except KeyboardInterrupt:
        logging.info("Consumer interrupted by user. Shutting down...")
    except KafkaException as e:
        logging.error(f"Kafka error occurred: {e}")
    except Exception as e:
        logging.error(f"Unexpected error: {e}")
    finally:
        logging.info("Closing consumer...")
        consumer.close()  # Ensure consumer is properly closed when done.