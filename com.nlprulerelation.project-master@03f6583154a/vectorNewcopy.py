global resultFrame;
def ruledesc(sRuleDesc):
    try :

        from sklearn.feature_extraction.text import CountVectorizer
        from sklearn.feature_extraction.text import TfidfTransformer
        from sklearn.feature_extraction.text import TfidfVectorizer
        from sklearn.metrics.pairwise import cosine_similarity  
        from nltk.corpus import stopwords
        import numpy as np
        import numpy.linalg as LA
        import pandas as pd
        from textblob import TextBlob
        from nltk import word_tokenize

        mylist = list()
        #InputSheet = "New Codes 2020 Q3.csv"
        MasterSheet = "QA_Library_Output.csv"
        resultFrame = pd.DataFrame(columns=('NewCode','NewCodeDesc','SameSimCode', 'CPTDesc','Accuracy','Comments'),index=range(10))
        rownum = -1
        NewCodeNum = -1

        def create_tokenizer_score(new_series, train_series, tokenizer):
            """
            return the tf idf score of each possible pairs of documents
            Args:
                new_series (pd.Series): new data (To compare against train data)
                train_series (pd.Series): train data (To fit the tf-idf transformer)
            Returns:
                pd.DataFrame
            """
            train_tfidf = tokenizer.fit_transform(train_series)
            
            new_tfidf = tokenizer.transform(new_series)

            X = pd.DataFrame(cosine_similarity(new_tfidf, train_tfidf), columns=train_series.index)
            X['ix_new'] = new_series.index
            score = pd.melt(
                X,
                id_vars='ix_new',
                var_name='ix_train',
                value_name='score'
            )
            return score


        def Get_MasterCodeDesc():
            df = pd.read_csv(MasterSheet)
            sMasterCodeDesc = df['SubRuleDesc'].tolist()
            return sMasterCodeDesc;

        def Get_SimCodeDesc():
            df = pd.read_csv(InputSheet,encoding ='latin1')
            sMasterCodeDesc = df['Sim_LongDesc'].tolist()
            return sMasterCodeDesc;

        def GetNewCPT():
            NewCodeFrame = pd.read_csv(InputSheet,encoding ='latin1')
            sNewCode = NewCodeFrame['MidRuleDotVersion'].tolist()
            return sNewCode;

        def GetNewCPTDesc():
            NewCodeFrame = pd.read_csv(InputSheet,encoding ='latin1')
            sNewCodeDesc = NewCodeFrame['SubRuleDesc'].tolist()
            return sNewCodeDesc;

        def GetSameSIMData():
            SameSimDf = pd.read_csv(InputSheet,encoding ='latin1')
            sNewCodeDesc = SameSimDf['SimCode'].tolist()
            return sNewCodeDesc;
        
        def textblob_tokenizer(str_input):
            blob = TextBlob(str_input.lower())
            tokens = blob.words
            words = [token.stem() for token in tokens]
            return words

        def GetRowNum(): 
            try:
                rownum = resultFrame.loc[(resultFrame.Accuracy == '0003T') & (resultFrame.Rank == 'CERVICOGRAPY')].index[0]
                    #print(rownum)
                resultFrame.loc[rownum,'Comments'] = "Test"
            except:
                "No record found"

        def GetMasterCPTCode(Val,NewCode,NewCodeDesc,Accuracy,Comment,x):
            df = pd.read_csv(MasterSheet)    
            GetCPTValue = df.iloc[Val,0]
            GetCPTDesc = df.iloc[Val,1]    
            mylist.append(GetCPTValue)
            mylist.append(GetCPTDesc)
            resultFrame.loc[x].NewCode = NewCode
            resultFrame.loc[x].SameSimCode = GetCPTValue
            resultFrame.loc[x].CPTDesc = GetCPTDesc
            resultFrame.loc[x].NewCodeDesc = NewCodeDesc
            resultFrame.loc[x].Accuracy = Accuracy
            resultFrame.loc[x].Comments = Comment
            return mylist;

        #train_CPT_Set = pd.Series(GetMasterCPT())
        train_set = pd.Series(Get_MasterCodeDesc())
        #SimCodeDescSet = pd.Series(Get_SimCodeDesc())
        #NewCodeDescSet = pd.Series(GetNewCPTDesc())
        #NewCodeSet = pd.Series(GetNewCPT())
        #SameSimSet = pd.Series(GetSameSIMData())

        NewCodeDescSet = [sRuleDesc]
        for i in NewCodeDescSet:
            NewCodeNum = NewCodeNum+1
            print(i)
            test_set = pd.Series(i)   
            
            #print(NewCodeSet[NewCodeNum])
            #print(i)
            test_set2 = word_tokenize(i)
            #tokenizer = TfidfVectorizer(stop_words = 'english')# initiate here your own tokenizer (TfidfVectorizer, CountVectorizer, with stopwords...)
            tokenizer = TfidfVectorizer()
            score = create_tokenizer_score(train_series=train_set, new_series=test_set, tokenizer=tokenizer)
            #score
            pd.set_option("display.max_rows", 11)
            sortedscore = score.sort_values(by=['score'], ascending=False)
            sortedscore.head(11)
            #print(resultFrame)
            RecNum =0
            for x in range(0,10):
                rownum = rownum+1
                svalue = sortedscore.iloc[x,1]
                #print(svalue)
                Accuracy = sortedscore.iloc[x,2]
                Comments = "Present in Autoproposal"
                result = GetMasterCPTCode(svalue,"",i,Accuracy*100,Comments,rownum)
                
                RecNum =RecNum +1
                # resultFrame
    except:
          print(" error occured")
    return  resultFrame