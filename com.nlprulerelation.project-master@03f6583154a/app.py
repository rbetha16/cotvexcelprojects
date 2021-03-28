import exceldata,vectorNew,vectorNewcopy
from flask import Flask,jsonify,render_template,request



app = Flask(__name__)


all_posts =[
    {
        'midrule'     : '32432',
        'midruledesc': 'This rule for cardiac'
    },
    {
        'midrule'     : '54357',
        'midruledesc': 'This rule for heart'
    }
]

@app.route('/medicaid')
def medicaidApp() :
    return render_template('index.html')

@app.route('/postdata',methods=['GET','POST'])
def posts() : 

    if request.method == 'POST':
         return "OPPS code pending____/\_____"
    else:     
         return render_template('posts.html',allposts=all_posts)    
     
@app.route('/name')
def dummyservice() :
    sval= "<h1>PYTHON SERVER UP & RUNNING 1</h1><p>This is Sample POC for Python service.</p>"
    return sval
@app.route('/jsondata')
def dummyJSONservice() :
    return jsonify(midrule="MID RULE DESC DATA")
#Paramertes for URI
@app.route('/midrule/<int:num>',methods=['GET'])
def getmidrule(num) :
    return jsonify({'Midrule result': num}),201

#########################################################################
# @app.route('/DataPreProcessing')
# def generateData():
#    return exceldata.ExcelDataPreProssing()
@app.route('/ruledesc/<string:ruledesc>')
def generateData(ruledesc):
   return jsonify(vectorNew.ruledesc(ruledesc))  
@app.route('/ruledesc1')
def generateData1():
   arg1 =request.args['rule']
   return jsonify(vectorNew.ruledesc(arg1))
         
#########################################################################

if(__name__ == "__main__") :
     app.run(debug=True)