function buildTable(dataList, valueTagName) {
  var timestamps = $(dataList).map(function() {return $(this).find("timestamp").text()});
  var tableHeader = buildTableHeader($.unique(timestamps))
  var tableBody = buildTableBody(dataList, valueTagName);
  var table = "<table class=\"table striped hovered cell-hovered bordered\">" +
    wrapByTag(wrapByTag(tableHeader, "tr", ""), "thead", "") +
    wrapByTag(tableBody, "tbody", "") +
    "</table>"
  return table;
}

function buildTableHeader(uniqueTimestamps) {
  var tableHeadersWithDate = $(uniqueTimestamps)
    .sort(byTimestampComparator)
    .map(function() {
      return Number(this);
    }).map(function() {
      return toHumanReadableDate(this);
    }).map(function() {
      return wrapByTag(this, "th", "");
    });
    return wrapByTag("", "th", "") + concatArray(tableHeadersWithDate, "");
}

function buildRow(rowTitle, dataList, valueTagName) {
  var tableCellsWithValues = dataList
    .sort(byTimestampComparator)
    .map(function() {
      return $(this).find(valueTagName).text();
    }).map(function() {
      return wrapByTag(this, "td", "");
    });
    return wrapByTag(rowTitle, "td", "") + concatArray(tableCellsWithValues, "");
}

function buildTableBody(dataList, valueTagName) {
  var tableRows = $(storesMeta).map(function() {
    var storeId = this.id;
    var dataForStore = $(dataList).filter(function(idx) {
      return Number($(this).find("storeId").text()) == storeId;
    })
    return buildRow(this.name, dataForStore, valueTagName);
  }).map(function() {
    return wrapByTag(this, "tr", "");
  });
  var tableContent = concatArray(tableRows, "");
  return tableContent;
}