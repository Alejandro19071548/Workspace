from app import create_app

from flask import Flask
from flask_cors import CORS
app = create_app()

if __name__ == '__main__':
    app.run(debug=True)


app = Flask(__name__)
CORS(app)