
from googlesearch import search

def find_chords(song_title, artist):
    query = f"{song_title} {artist} guitar chords site:ultimate-guitar.com"

    for result in search(query, num_results=1):
        return result  # Return the first search result

    return "No results found."
'''
def find_chords(song_title, artist):
    # Format the query for Ultimate Guitar's search
    query = f"{song_title} {artist} guitar chords"
    search_url = f"https://www.ultimate-guitar.com/search.php?search_type=title&value={query.replace(' ', '+')}"

    # Open the search results in the user's default web browser
    #webbrowser.open(search_url)
    return search_url  # Return the search URL for reference
'''