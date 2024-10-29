import sys
from deep_translator import GoogleTranslator

class Translator:
    def __init__(self):
        self.translator = GoogleTranslator(source='en', target='ko')

    def translate_sentence(self, sentence):
        try:
            return self.translator.translate(sentence)
        except Exception as e:
            return sentence

if __name__ == "__main__":
    translator = Translator()
    for line in sys.stdin:
        sentence = line.strip()
        if sentence:
            translation = translator.translate_sentence(sentence)
            print(translation)