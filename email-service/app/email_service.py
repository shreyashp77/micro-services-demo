import smtplib
from email.message import EmailMessage
from jinja2 import Environment, FileSystemLoader
import os

TEMPLATE_DIR = os.path.join(os.path.dirname(__file__), 'templates')
env = Environment(loader=FileSystemLoader(TEMPLATE_DIR))

def send_email(recipient_email: str, order_id: str, smtp_server: str, smtp_port: int, sender_email: str):
    template = env.get_template("order_email.html")
    html_content = template.render(order_id=order_id)

    msg = EmailMessage()
    msg['Subject'] = "Your Order Confirmation"
    msg['From'] = sender_email
    msg['To'] = recipient_email
    msg.set_content("Your email client does not support HTML.")
    msg.add_alternative(html_content, subtype='html')

    with smtplib.SMTP(smtp_server, smtp_port) as server:
        server.send_message(msg)
        print(f"Email sent to {recipient_email}")