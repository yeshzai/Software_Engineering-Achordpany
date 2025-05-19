import re
import requests
from googlesearch import search
from concurrent.futures import ThreadPoolExecutor, as_completed

def smart_clean_title(title):
    title = re.sub(r"\s*\(.*?\)", "", title)
    title = re.sub(r"\bNo\.\s*\d+\b", "", title)
    title = re.sub(r"\bOp\.\s*\d+.*?", "", title)
    title = re.sub(r"\s+", " ", title).strip()
    title = title.split(":")[0].strip()
    return title

def generate_songs(lyrics):
    api_url = f"https://itunes.apple.com/search?term={lyrics}&entity=song&limit=5"
    response = requests.get(api_url)
    if response.status_code == 200:
        data = response.json()
        songs = [smart_clean_title(track["trackName"]) for track in data.get("results", [])]
        return songs if songs else None
    else:
        return None

def find_chords(song_title):
    chord_sites = [
        "ultimate-guitar.com",
        "e-chords.com",
        "chordie.com",
        "guitartabs.cc",
        "chordify.net"
    ]
    for site in chord_sites:
        query = f'{song_title} guitar chords site:{site}'
        try:
            for result in search(query, num_results=1):
                return result  # Return the first URL found
        except Exception as e:
            print(f"Error searching {site}: {e}")
            continue
    return None

def check_song(song):
    #print(f"Checking: {song}...")
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

    if valid_songs:
        for title, url in valid_songs.items():
            theURL = "" + url
            match = re.search(r"/tab/([^/]+)/", theURL)
            artist = ""
            if(match):
                artist = match.group(1)
                artist = artist.replace("-", " ")
                artist = artist.title()

            title_artist_url = title + "<00>" + artist + "<00>" + url
            title_artist_url_list.append(title_artist_url)

    counter = 0
    for i in title_artist_url_list:
        parts = i.split("<00>")
        url = parts[2]
        if "tabs.ultimate-guitar.com/tab/" not in url:
            title_artist_url_list.pop(counter)
        counter += 1

    return title_artist_url_list

# RETURNS:
'''
"SONG TITLE<00>ARTIST<00>URL"
'''