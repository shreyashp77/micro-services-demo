from dotenv import load_dotenv
from app.consumer import start_consumer

def main():
    load_dotenv()
    start_consumer()

if __name__ == "__main__":
    main()