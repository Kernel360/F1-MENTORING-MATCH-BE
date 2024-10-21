import os
import re
from collections import Counter

import matplotlib.pyplot as plt
import nltk
import pandas as pd
from nltk.corpus import stopwords, words
from wordcloud import WordCloud


# Download NLTK stopwords and word list (required on first run)
# nltk.download('stopwords')
# nltk.download('words')

# Extract "text" sections from a file
def extract_text(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()
    text_segments = re.findall(r'"text":\s*"([^"]+)"', content)
    full_text = ' '.join(text_segments)
    return full_text


# Calculate word frequency and verify against dictionary
def calculate_word_frequency(text):
    # Convert to lowercase and remove special characters
    words_list = re.findall(r'\b\w+\b', text.lower())
    # Use NLTK's stopword list
    stop_words = set(stopwords.words('english'))
    # Use NLTK's list of English words
    english_words = set(words.words())

    # Remove numbers or meaningless words and check if they are in the dictionary
    valid_words = [word for word in words_list if
                   word not in stop_words and word.isalpha() and len(word) > 2 and word in english_words]
    word_count = Counter(valid_words)
    return word_count


# Classify word difficulty levels
def classify_difficulty(word_count, total_words):
    difficulty_levels = {}

    for word, freq in word_count.items():
        frequency_ratio = freq / total_words
        if frequency_ratio > 0.02:
            difficulty_levels[word] = 'Easy'
        elif frequency_ratio > 0.008:
            difficulty_levels[word] = 'Medium'
        else:
            difficulty_levels[word] = 'Hard'

    return difficulty_levels


# Organize results into a DataFrame and save to a file
def save_word_data_to_csv(word_count, difficulty_levels, output_file):
    # Create DataFrame
    df = pd.DataFrame(word_count.items(), columns=['Word', 'Frequency'])
    df['Difficulty'] = df['Word'].map(difficulty_levels)

    # Save to CSV file (append to existing file)
    if os.path.exists(output_file):
        existing_df = pd.read_csv(output_file)
        df = pd.concat([existing_df, df]).groupby('Word').agg({'Frequency': 'sum', 'Difficulty': 'last'}).reset_index()

    # Sort by frequency in descending order
    df = df.sort_values(by='Frequency', ascending=False).reset_index(drop=True)

    df.to_csv(output_file, index=False)
    print(f"Word data saved to {output_file}")


# Analyze new text files and update data
def update_word_data(directory_path, output_file, processed_files_log):
    total_word_count = Counter()

    # Load list of processed files if it exists
    if os.path.exists(processed_files_log):
        with open(processed_files_log, 'r') as f:
            processed_files = set(f.read().splitlines())
    else:
        processed_files = set()

    # Find all .txt files in the directory
    file_paths = [os.path.join(directory_path, f) for f in os.listdir(directory_path) if f.endswith('.txt')]

    for file_path in file_paths:
        if file_path not in processed_files:
            full_text = extract_text(file_path)
            word_frequency = calculate_word_frequency(full_text)
            total_word_count.update(word_frequency)

            # Mark this file as processed
            with open(processed_files_log, 'a') as f:
                f.write(file_path + '\n')

    total_words = sum(total_word_count.values())
    difficulty_levels = classify_difficulty(total_word_count, total_words)

    save_word_data_to_csv(total_word_count, difficulty_levels, output_file)


def visualize_word_frequency(data_file):
    # Load data from CSV file
    df = pd.read_csv(data_file)

    # Select the top 10 words by frequency
    top_words = df.head(10)

    # Create a bar chart for word frequency
    plt.figure(figsize=(10, 6))
    plt.bar(top_words['Word'], top_words['Frequency'], color='skyblue')
    plt.title('Top 10 Most Frequent Words')
    plt.xlabel('Words')
    plt.ylabel('Frequency')
    plt.xticks(rotation=45)
    plt.tight_layout()

    # Display the chart
    plt.show()


def visualize_word_cloud(data_file):
    # Load data from CSV file
    df = pd.read_csv(data_file)

    # Generate a word cloud image
    wordcloud = WordCloud(width=800, height=400, background_color='white').generate_from_frequencies(
        dict(zip(df['Word'], df['Frequency'])))

    # Display the generated image
    plt.figure(figsize=(10, 5))
    plt.imshow(wordcloud, interpolation='bilinear')
    plt.axis('off')
    plt.title('Word Cloud')
    plt.show()


# Specify directory and log files to process and update data
directory_path = './'  # Specify the path to the directory containing files to analyze.
output_file = 'word_data.csv'
processed_files_log = 'processed_files.log'

update_word_data(directory_path, output_file, processed_files_log)
visualize_word_frequency(output_file)
visualize_word_cloud(output_file)