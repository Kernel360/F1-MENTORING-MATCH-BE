import packages as p


# 방해 단어 생성 함수 개선
def get_distractors(word, pos, num_distractors=3):
    distractors = set()
    synsets = p.wn.synsets(word, pos=pos)

    if synsets:
        lemmas = synsets[0].lemmas()
        for lemma in lemmas:
            name = lemma.name().replace('_', ' ')
            if name.lower() != word.lower():
                # Tokenize and tag the potential distractor to check its POS
                name_tokens = p.nltk.word_tokenize(name)
                name_pos_tags = p.nltk.pos_tag(name_tokens)
                # Ensure it is not a proper noun (NNP or NNPS)
                if not any(tag.startswith('NNP') for _, tag in name_pos_tags):
                    distractors.add(name)
            if len(distractors) >= num_distractors:
                break
    return list(distractors)


# 주요 품사 태깅을 통한 빈칸 단어 선택
def select_word_to_blank(tokens, pos_tags):
    # 명사(NN), 동사(VB), 형용사(JJ), 부사(RB) 우선 선택
    candidate_indices = [i for i, (word, pos) in enumerate(pos_tags)
                         if pos.startswith(('NN', 'VB', 'JJ', 'RB')) and len(word) > 3]
    if candidate_indices:
        return p.random.choice(candidate_indices)
    return None


# 객관식 문제 생성 함수 개선
def generate_fill_in_the_blank_mcq(sentence, num_options=4):
    fill_blank_mcqs = []
    tokens = p.nltk.word_tokenize(sentence)
    pos_tags = p.nltk.pos_tag(tokens)

    blank_index = select_word_to_blank(tokens, pos_tags)
    if blank_index is None:
        return fill_blank_mcqs

    correct_word = tokens[blank_index]
    pos = pos_tags[blank_index][1][0].lower()  # 첫 글자만 사용

    # 품사에 맞는 WordNet POS 태그 매핑
    pos_map = {'n': p.wn.NOUN, 'v': p.wn.VERB, 'a': p.wn.ADJ, 'r': p.wn.ADV}
    wordnet_pos = pos_map.get(pos, p.wn.NOUN)

    distractors = get_distractors(correct_word, wordnet_pos, num_options - 1)
    if len(distractors) < num_options - 1:
        return fill_blank_mcqs

    options = [correct_word] + distractors
    p.random.shuffle(options)

    tokens_copy = tokens[:]
    tokens_copy[blank_index] = '_____'
    question_sentence = ' '.join(tokens_copy)

    fill_blank_mcqs.append({
        'question': question_sentence,
        'options': options,
        'answer': correct_word
    })

    return fill_blank_mcqs


# Excel에서 핵심 문장 불러오기 및 문제 생성
def generate_mcqs(file_path='data/output_data.xlsx', num_options=4):
    key_sentences_df = p.pd.read_excel(file_path, usecols=['Key Sentences'])
    mcqs = []

    for kss in key_sentences_df['Key Sentences']:
        sentences_list = eval(kss) if isinstance(kss, str) else kss
        for sentence in sentences_list:
            fill_blank_mcqs = generate_fill_in_the_blank_mcq(sentence, num_options)
            mcqs.extend(fill_blank_mcqs)

    return mcqs


# 생성된 문제 출력
if __name__ == "__main__":
    mcqs = generate_mcqs()
    for mcq in mcqs:
        print(f"문제: {mcq['question']}")
        for idx, option in enumerate(mcq['options'], 1):
            print(f"{idx}. {option}")
        print(f"정답: {mcq['answer']}\n")
