
def ExcelDataPreProssing():
    try :
            import pandas
            import pandas as pd
            from pandas import ExcelWriter
            from pandas import ExcelFile
            resultFrame = pd.DataFrame(columns=('MidRuleDotVersion','SubRuleDesc'),index=range(7000))        
            df =pd.read_excel(r'H:\NLP\QA_Library_7.24.2019.xlsx',sheet_name='Report 1')    
            #print (df[sColumn].index)
            sFinalval =''
            rownum=0
            for i in df['Sub Rule Desc'].index:
                            #print(df['Sub Rule Desc'][i])
            #                     print(i)
                                sGetval=df['Sub Rule Desc'][i]
                                counteropen=0
                                counterclose=0
                                blnresult=False 
            #                     print(sGetval)
                                if sGetval.count('(')==sGetval.count(')'):
                                    for j in sGetval:
                                        if (j.find("(")):
                                            counteropen+=1
                                            blnresult=True
                                        if (j.find(")")): 
                                            counterclose+=1
                                            blnresult=True

                                        if(counteropen==counterclose) :
                                            blnresult=False
                                        if(blnresult==False):
                #                             sFinalval=sFinalval+j
                                            sFinalval=sFinalval+j.replace(")","")                            
                #                     print(i)
                                    print(sFinalval)
                                    resultFrame.loc[i].MidRuleDotVersion = "test"
                                    resultFrame.loc[i].SubRuleDesc =sFinalval
                #                     sFinalval=''
                                    resultFrame
                                else:
                                    resultFrame.loc[i].MidRuleDotVersion = "Not Match"
                                    resultFrame.loc[i].SubRuleDesc =sGetval
                                

            resultFrame 
            resultFrame.to_csv ('Del-Generated.csv', index = None, header=True)
    except:
          print(" error occured")
    return
