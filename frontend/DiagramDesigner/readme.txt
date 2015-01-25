Der DiagramDesigner ist eine externe Library, welche grundlegende Funktionen für einen grafischen UML-Editor bereitstellt. 
Der Quellcode stammt von http://www.codeproject.com/Articles/484616/MVVM-Diagram-Designer, welcher dort von Sacha Barber unter der CPOL-Lizenz (http://www.codeproject.com/info/cpol10.aspx) zur Verfügung gestellt wird.

Die Library beinhaltet unter anderem folgende wichtige Funktionen:
- Drag & Drop von Elementen aus einer Sidebar in einen Canvas
- Löschen von Elementen im Canvas
- Verbindungen zwischen Elementen ziehen
- Wegfindungs-Algorithmus um Überschneidungen bei Verbindungen bestmöglich zu vermeiden

Um die Library unseren Bedürfnissen anzupassen, mussten wir an einigen Stellen kleine Codeänderungen vornehmen.
Geändert wurden unter Anderem:
* ViewModels/DiagramViewModel.cs
* ViewModels/DesignerItemViewModelBase.cs
* Controls/DesignerCanvas.cs
* Controls/DragThumb.cs

Weiterhin wurden für die Lesbarkeit einzelne Variablennamen geändert.