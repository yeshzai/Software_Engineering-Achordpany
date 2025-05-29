import requests

API_KEY = "AIzaSyC7NfF-v7AQg7QRIrSkvMdPTeMauf7vjxE"
CSE_ID = "71744b8e3733749a8"

def find_chords(song_title):
    query = f"{song_title} guitar chords site:ultimate-guitar.com"

    url = "https://www.googleapis.com/customsearch/v1"
    params = {
        "key": API_KEY,
        "cx": CSE_ID,
        "q": query,
        "num": 1
    }

    try:
        response = requests.get(url, params=params)
        response.raise_for_status()
        results = response.json()
        if "items" in results and results["items"]:
            return results["items"][0]["link"]
    except Exception as e:
        print(f"Error: {e}")
        return None
