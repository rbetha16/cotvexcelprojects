

On Error Resume Next

Set xlApp = CreateObject("Excel.Application")
xlApp.DisplayAlerts = False

sDestinationPath = WScript.Arguments(0)
sDate = WScript.Arguments(1)
sExcelPath = WScript.Arguments(2)

Set xlBook = xlApp.Workbooks.Open(sExcelPath)
xlApp.Run "Trigger",sDestinationPath,sDate
xlApp.Quit

Set xlBook = Nothing
Set xlApp = Nothing