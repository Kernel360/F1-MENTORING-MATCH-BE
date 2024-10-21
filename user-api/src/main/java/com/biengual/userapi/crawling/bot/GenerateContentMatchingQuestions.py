import CalculatorQuestionDifficulty as cqd
import packages as p

# Initialize the tokenizer and model separately
tokenizer = p.AutoTokenizer.from_pretrained("valhalla/t5-base-qg-hl", legacy=False)
model = p.AutoModelForSeq2SeqLM.from_pretrained("valhalla/t5-base-qg-hl")

# Create the pipeline using the tokenizer and model
question_generation_pipeline = p.pipeline(
    "text2text-generation",
    model=model,
    tokenizer=tokenizer,
    clean_up_tokenization_spaces=True
)

similarity_model = p.SentenceTransformer('paraphrase-MiniLM-L6-v2')

# Generate questions from sentences
def generate_questions(sentences, num_questions=5):
    questions = []
    for sentence in sentences:
        input_text = f"generate questions: {sentence[:512]}"  # Limit input length to 512 characters
        outputs = question_generation_pipeline(
            input_text,
            max_length=150,
            num_return_sequences=num_questions,
            num_beams=max(4, num_questions),
            clean_up_tokenization_spaces=True
        )
        for output in outputs:
            question = output['generated_text'].strip()
            if question and all(
                    p.cosine_similarity(similarity_model.encode([question]), similarity_model.encode([q]))[0][0] < 0.8 for q in questions):
                questions.append(question)
    return questions[:num_questions]

# Function to generate model answers and keywords from key sentences
def generate_model_answers_and_keywords(sentences):
    model_answers_keywords = []
    for sentence in sentences:
        # Use the sentence itself as a model answer
        model_answer = sentence

        # Extract keywords by selecting nouns and proper nouns from the sentence
        words = p.nltk.word_tokenize(sentence)
        pos_tags = p.nltk.pos_tag(words)
        keywords = [word for word, pos in pos_tags if pos in ('NN', 'NNP')]

        model_answers_keywords.append({
            'model_answer': model_answer,
            'keywords': keywords
        })
    return model_answers_keywords

# Main execution
if __name__ == "__main__":
    print("\nGenerated Questions:")
    base_score = 10  # Define a base score for each question
    file_path = 'data/output_data.xlsx'
    key_sentences_df = p.pd.read_excel(file_path, usecols=['Key Sentences'])

    for kss in key_sentences_df.itertuples(index=False):
        questions = generate_questions(kss)  # Pass as a list of one element
        model_answers_keywords = generate_model_answers_and_keywords(kss)

        for i, question in enumerate(questions, 1):
            # Print corresponding model answer and keywords
            if i <= len(model_answers_keywords):
                keywords = model_answers_keywords[i - 1]['keywords']
                difficulty_level, multiplier = cqd.calculate_question_difficulty(question, keywords, 4)  # Assuming 4 options for MCQs
                score = cqd.calculate_score_based_on_difficulty(base_score, multiplier)

                print(f"{i}. {question}")
                print(f"Model Answer: {model_answers_keywords[i - 1]['model_answer']}")
                print(f"Keywords: {', '.join(keywords)}")
                print(f"Difficulty Level: {difficulty_level}, Assigned Score: {score}\n")