# TODO: 아직 완전히 적용되지 않음

# Function to dynamically calculate question difficulty
def calculate_question_difficulty(question_text, keywords, options_count):
    # Adjusted difficulty score calculation
    word_count = len(question_text.split())
    keyword_complexity = len(keywords) / max(1, word_count)  # Avoid division by zero
    difficulty_score = (word_count * 0.25) + (options_count * 0.6) - (keyword_complexity * 0.15)

    # More granular difficulty levels
    if difficulty_score < 2:
        return 'Very Easy', 0.5
    elif difficulty_score < 3:
        return 'Easy', 1
    elif difficulty_score < 4:
        return 'Medium', 1.5
    elif difficulty_score < 5:
        return 'Hard', 2
    else:
        return 'Very Hard', 2.5
# Function to implement a flexible scoring system
def calculate_score_based_on_difficulty(base_score, multiplier):
    # Directly use the multiplier to calculate score
    return base_score * multiplier
