import re
import requests
from concurrent.futures import ThreadPoolExecutor, as_completed

API_KEY = "AIzaSyC7NfF-v7AQg7QRIrSkvMdPTeMauf7vjxE"
CSE_ID = "71744b8e3733749a8"

def smart_clean_title(title):
    title = re.sub(r"\s*\(.*?\)", "", title)
    title = re.sub(r"\bNo\.\s*\d+\b", "", title)
    title = re.sub(r"\bOp\.\s*\d+.*?", "", title)
    title = re.sub(r"\s+", " ", title).strip()
    title = title.split(":")[0].strip()
    return title

def generate_songs(lyrics):
    api_url = f"https://itunes.apple.com/search?term={lyrics}&entity=song&limit=3"
    response = requests.get(api_url)
    if response.status_code == 200:
        data = response.json()
        songs = [smart_clean_title(track["trackName"]) for track in data.get("results", [])]
        print(songs)
        return songs if songs else None
    else:
        return None

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
        print(f"Error searching Ultimate Guitar: {e}")
    return None

def check_song(song):
    link = find_chords(song)
    if link:
        return (song, link)
    else:
        return None

def generate_recommendations(genre):
    songs = generate_songs(genre)
    valid_songs = {}

    with ThreadPoolExecutor(max_workers=5) as executor:
        future_to_song = {executor.submit(check_song, song): song for song in songs}
        for future in as_completed(future_to_song):
            result = future.result()
            if result:
                song_title, link = result
                valid_songs[song_title] = link

    title_artist_url_list = []

    for title, url in valid_songs.items():
        artist = ""
        match = re.search(r"/tab/([^/]+)/", url)
        if match:
            artist = match.group(1).replace("-", " ").title()

        title_artist_url = f"{title}<00>{artist}<00>{url}"
        title_artist_url_list.append(title_artist_url)

    # Filter only URLs that match the standard UG tab format
    title_artist_url_list = [
        entry for entry in title_artist_url_list
        if "tabs.ultimate-guitar.com/tab/" in entry.split("<00>")[2]
    ]

    return title_artist_url_list

# RETURNS:
'''
"SONG TITLE<00>ARTIST<00>URL"
'''
