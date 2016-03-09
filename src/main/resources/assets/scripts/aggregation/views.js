function buildTable(dataList, rowNames) {
  var data = transpose(dataList);
  var timestamps = data[0];
  var tableHeader = buildTableHeader(timestamps);
  var noTimestamps = data.slice();
  noTimestamps.shift();
  var tableBody = buildTableBody(noTimestamps, rowNames);
  var table = "<table class=\"table striped hovered cell-hovered bordered\">" +
    wrapByTag(wrapByTag(tableHeader, "tr", ""), "thead", "") +
    wrapByTag(tableBody, "tbody", "") +
    "</table>"
  return table;
}

function buildTableHeader(timestamps) {
  var tableHeadersWithDate = $(timestamps)
    .map(function() {
      return toHumanReadableDate(this)
    }).map(function() {
      return wrapByTag(this, "th", "");
    });
    return wrapByTag("", "th", "") + concatArray(tableHeadersWithDate, "");
}

function buildRow(row, name) {
  var tableCellsWithValues = row
    .map(function(value) {
      return wrapByTag(value, "td", "");
    });
    return wrapByTag(name, "td", "") + concatArray(tableCellsWithValues, "");
}

function buildTableBody(dataList, rowNames) {
  var tableRows = $(dataList).map(function(i) {
    return buildRow(this, rowNames[i]);
  }).map(function() {
    return wrapByTag(this, "tr", "");
  });
  var tableContent = concatArray(tableRows, "");
  return tableContent;
}

function buildGraph(dataList, selector) {
  var canvas = "<canvas id=\"graph\" width=\"700px\" height=\"400px\"></canvas>";
  //var graphContext = document.getElementById("graph").getContext("2d");
  //var graphData = buildGraphDataSet(dataList, selector);
  //var graph = new Chart(graphContext).Line({}, {});
  return canvas;
}

function buildGraphDataSet(dataList, selector) {
  var timestamps = $(dataList).map(function() {return $(this).find("timestamp").text()});
  var labels = buildGraphLabels($.unique(timestamps))
  var colors = randomColor({luminosity: "bright", count: storesMeta.length, format: "rgbArray"});
  var graphLines = $(storesMeta).map(function(idx) {
    var storeId = this.id;
    var dataForStore = $(dataList).filter(function(idx) {
      return Number($(this).find("storeId").text()) == storeId;
    })
    var lineDataForStore =  buildGraphLineData(dataForStore, selector);
    var rgbPartOfColor = colors[idx][0] + ", " + colors[idx][1] + ", " + colors[idx][2]
    return {
      label: this.name,
      fillColor: "rgba(" + rgbPartOfColor + ", 0.2)",
      strokeColor: "rgba(" + rgbPartOfColor + ", 0.8)",
      pointColor: "rgba(" + rgbPartOfColor + ", 1)",
      pointStrokeColor: "#fff",
      pointHighlightFill: "#fff",
      pointHighlightStroke: "rgba(" + rgbPartOfColor + ", 1)",
      data: lineDataForStore
    }
  }).get();
  return {
    labels: labels,
    datasets: graphLines
  };
}

function buildGraphLabels(uniqueTimestamps) {
  return $(uniqueTimestamps)
    .sort(byTimestampComparator)
    .map(function() {
      return Number(this);
    }).map(function() {
      return toHumanReadableDate(this);
    }).get();
}

function buildGraphLineData(dataList, selector) {
  return dataList
    .sort(byTimestampComparator)
    .map(function() {
      return $(this).find(selector).text();
    }).map(function() {
      return parseFloat(this)
    }).get();
}