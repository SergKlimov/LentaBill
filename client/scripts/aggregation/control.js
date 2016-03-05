function buildSwitcher() {
  var tableViewIndicator = "<button class=\"button primary\" onclick=\"switchView(this)\">table</button>";
  var graphViewIndicator = "<button class=\"button\" onclick=\"switchView(this)\">graph</button>";
  return "<div align=\"right\">" + tableViewIndicator + graphViewIndicator + "</div>";
  //return wrapByTag(tableViewIndicator + graphViewIndicator, "div", "");
}

function switchView(btn) {
  if ($(btn).hasClass("primary")) {
    return ;
  }
  var aggregationView = $(btn).parents(".aggregation-view-content")
  var currentMode = $(aggregationView).attr("mode");
  if (currentMode == "table") {
    $(aggregationView).find(".aggregation-table-view").hide();
    $(aggregationView).find(".aggregation-graph-view").show();
    $(aggregationView).attr("mode", "graph");
  } else if (currentMode == "graph") {
    $(aggregationView).find(".aggregation-table-view").show();
    $(aggregationView).find(".aggregation-graph-view").hide();
    $(aggregationView).attr("mode", "table");
  }
  $(btn).addClass("primary");
  $(btn).siblings(".button").removeClass("primary");
}

function buildController() {
  var input = "<input value=\"" + toHumanReadableDate(dataDomain.lb) + "\" readonly>";
  var wrappedInput = wrapByTag(input, "div", ["input-control", "text"])
  var slider = "<div class=\"slider large\" data-role=\"slider\" " +
    "data-min-value=" + dataDomain.lb + " " +
    "data-max-value=" + dataDomain.ub + " " +
    "data-accuracy=" + (100 * (24 * 60 * 60 * 1000) / (dataDomain.ub - dataDomain.lb)) + " " +
    "data-on-change=updateAggregationView " +
    "></div>"
  return wrappedInput + slider
}

function updateAggregationView(v, slider) {
  var aggregationView = $(slider).parent();
  var controlDisplay = $(aggregationView).find('input')[0];
  var currentDisplayValue = $(controlDisplay).val();
  var updateDisplayValue = toHumanReadableDate(v)
  if (currentDisplayValue == updateDisplayValue) {
    return ;
  }
  $(controlDisplay).val(updateDisplayValue)
  var d = new Date(v);
  var truncated = new Date(d.getFullYear(), d.getMonth(), d.getDate(), 0, 0, 0)
  $.ajax({
    url: address + "/api/checks/aggregation/byDateAndStore",
    method: "GET",
    contentType: "application/xml",
    data: {since: toMilliseconds(updateDisplayValue), limit: 7},
    success: function (d) {
      var fullDataSet = $(d).find("checksAggregationResultRepresentation");

      var selector = aggregationView.attr("selector");
      var table = buildTable(fullDataSet, selector);
      $(aggregationView).find(".aggregation-table-view").html(table);

      var graphContext = $(aggregationView).find("#graph")[0].getContext("2d");
      graphContext.canvas.width = aggregationView.width() - 30;
      var graphData = buildGraphDataSet(fullDataSet, selector);
      var graph = new Chart(graphContext).Line(graphData, {});
    }
  });
}