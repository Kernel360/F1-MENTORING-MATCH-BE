# -*- coding: utf-8 -*-
import random
import re
import nltk
import pandas as pd
import os
import java.nio.charset.Charset

from nltk.corpus import wordnet as wn
from nltk.corpus import stopwords

from sentence_transformers import SentenceTransformer

from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

from transformers import pipeline, AutoTokenizer, AutoModelForSeq2SeqLM

from openpyxl.workbook import Workbook

from nltk.tokenize import sent_tokenize, word_tokenize

from deep_translator import GoogleTranslator  # Import Google Translator from deep_translator
