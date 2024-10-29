import packages as p


# # Download necessary NLTK data
# nltk.download('punkt')
# nltk.download('wordnet')
# nltk.download('averaged_perceptron_tagger')

# Function to check if a file has been processed
def is_file_processed(file_name):
    with open(preprocessed_files_log, 'r') as log_file:
        processed_files = log_file.read().splitlines()
    exists = file_name in processed_files
    if exists:
        print(f'{file_name} has already been processed. Checking log...')
    return exists

# Function to mark a file as processed
def mark_file_as_processed(file_name):
    with open(preprocessed_files_log, 'a') as log_file:
        log_file.write(file_name + '\n')

# Text preprocessing and tokenization
def preprocess_text(text):
    sentences = p.nltk.sent_tokenize(text)
    return sentences

# Extract key sentences using TF-IDF, selecting a percentage of total sentences
def extract_key_sentences(text, percentage=0.3):
    sentences = preprocess_text(text)
    vectorizer = p.TfidfVectorizer()
    tfidf_matrix = vectorizer.fit_transform(sentences)
    sentence_scores = tfidf_matrix.sum(axis=1).A1
    num_sentences = max(1, int(len(sentences) * percentage))
    ranked_sentences = [sentences[i] for i in sentence_scores.argsort()[-num_sentences:][::-1]]
    return ranked_sentences

# Extract key paragraphs using TF-IDF, selecting a percentage of total paragraphs
def extract_key_paragraphs(text, percentage=0.2):
    paragraphs = p.re.split(r'\n\s*\n', text)  # 문단을 빈 줄 기준으로 분할
    paragraphs = [para.strip() for para in paragraphs if para.strip()]
    if not paragraphs:
        return []
    vectorizer = p.TfidfVectorizer()
    tfidf_matrix = vectorizer.fit_transform(paragraphs)
    paragraph_scores = tfidf_matrix.sum(axis=1).A1
    num_paragraphs = max(1, int(len(paragraphs) * percentage))
    ranked_paragraphs = [paragraphs[i] for i in paragraph_scores.argsort()[-num_paragraphs:][::-1]]
    return ranked_paragraphs


# Extract text from file
def extract_text_from_file(file_path):
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            content = file.read()
        text_segments = p.re.findall(r'"text":\s*"([^"]+)"', content)
        full_text = ' '.join(text_segments)
        full_text = p.re.sub(r'\\n', ' ', full_text)
        full_text = p.re.sub(r'\\', '', full_text)
        full_text = p.re.sub(r'\s+', ' ', full_text).strip()
        return full_text
    except FileNotFoundError:
        print(f"File not found.: {file_path}")
        return ""


# Save extracted_text and key_sentences to a file
def save_to_file(extracted_text, key_sentences, key_paragraphs, output_file_path):
    try:
        with open(output_file_path, 'w', encoding='utf-8') as file:
            # Write the extracted text
            file.write("Extracted Text:\n")
            file.write(extracted_text + "\n\n")

            # Write the key sentences
            file.write("Key Sentences:\n")
            for sentence in key_sentences:
                file.write(sentence + "\n")

            # Write the key paragraphs
            file.write("Key Paragraphs:\n")
            for paragraph in key_paragraphs:
                file.write(paragraph + "\n")

        print(f"Data successfully saved to {output_file_path}")
    except Exception as e:
        print(f"An error occurred while saving to file: {e}")



# List of files to process
files_name = ['paste.txt', 'paste(0).txt', 'paste(1).txt', 'paste(2).txt']
files_path = [f'data/{file}' for file in files_name]

# Specify the output file path for Excel
output_file_path_excel = 'data/output_data.xlsx'
preprocessed_files_log = 'data/log/preprocessed_files_log.log'

# Ensure the directory exists, if not, create it
p.os.makedirs(p.os.path.dirname(preprocessed_files_log), exist_ok=True)

# Create the log file if it does not exist
if not p.os.path.exists(preprocessed_files_log):
    with open(preprocessed_files_log, 'w') as log_file:
        log_file.write('')  # Create an empty log file if it doesn't exist

# List to store DataFrame rows
rows_list = []

for file_path in files_path:
    # Check if the file has already been processed
    if is_file_processed(file_path):
        print(f"Skipping {file_path} as it has already been processed.")
        continue

    extracted_text = extract_text_from_file(file_path)

    # Check if the extracted text is empty
    if not extracted_text:
        print(f"No text extracted from {file_path}. Skipping.")
        continue

    key_sentences = extract_key_sentences(extracted_text)
    key_paragraphs = extract_key_paragraphs(extracted_text)

    file_name = p.os.path.basename(file_path)

    # Log processed file
    mark_file_as_processed(file_name)

    # Create a dictionary for the new row and append to the list
    row_dict = {
        'File Name': file_name,
        'Extracted Text': extracted_text,
        'Key Sentences': key_sentences,
        'Key Paragraphs': key_paragraphs
    }
    rows_list.append(row_dict)

# Create a DataFrame from the list of rows
df = p.pd.DataFrame(rows_list)

# Save the DataFrame to an Excel file
df.to_excel(output_file_path_excel, index=False)  # Save without the index

print(f"Data successfully saved to {output_file_path_excel}")
