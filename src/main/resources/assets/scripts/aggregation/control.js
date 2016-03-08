var from = 0, limit = 0, fromForec = 0, limitForec = 0;
function buildSwitcher() {
  var tableViewIndicator = "<button class=\"button primary\" onclick=\"switchView(this)\">table</button>";
  var graphViewIndicator = "<button class=\"button\" onclick=\"switchView(this)\">graph</button>";
  return "<div align=\"right\">" + tableViewIndicator + graphViewIndicator + "</div>";
  //return wrapByTag(tableViewIndicator + graphViewIndicator, "div", "");
}

function showTable() {
  $("#tableBlock").show();
  $("#chart_div").hide();
  $("#tableBlockForec").show();
  $("#chart_divForec").hide();

  $("#tableButton").addClass("active");
  $("#chartButton").removeClass("active");
  showSlider();
}

function showChart() {
  $("#chart_div").show();
  $("#tableBlock").hide();
  $("#chart_divForec").show();
  $("#tableBlockForec").hide();

  $("#chartButton").addClass("active");
  $("#tableButton").removeClass("active");
  //chart.draw(data, chart_options);
  showSlider();
}

function showSlider () {
  var changeSlider = function (data) {
    createSlider(data);
    createReport();
  };
  var createSlider = function (data) {
    from = data.from;
    limit = moment(data.to, "X").diff(moment(data.from, "X"), 'days');
  };
  var changeSliderForec = function (data) {
    fromForec = data.from;
    limitForec = moment(data.to, "X").diff(moment(data.from, "X"), 'days');
    createReport()
  };
  $("#range").ionRangeSlider({
    type: 'double',
    grid: true,
    force_edges: true,
    dragRange: false,
    min: +moment().subtract(1, "months").format("X"),
    max: +moment().format("X"),
    from: +moment().subtract(1, "months").format("X"),
    to: +moment().subtract(1, "months").add(6, "days").format("X"),
    prettify: function (num) {
      return moment(num, "X").format("Do MMMM");
    },
    onStart: createSlider,
    onChange: changeSlider,
  });
  $("#rangeForec").ionRangeSlider({
    type: 'double',
    grid: true,
    force_edges: true,
    dragRange: false,
    min: +moment().format("X"),
    max: +moment().add(15, "days").format("X"),
    from: +moment().format("X"),
    to: +moment().add(15, "days").format("X"),
    prettify: function (num) {
      return moment(num, "X").format("Do MMMM");
    },
    onStart: changeSliderForec,
    onChange: changeSliderForec,
  });
}
function updateAggregationView(v, slider) {
  var aggregationView = $(slider).parent();
  var controlDisplay = $(aggregationView).find('input')[0];
  var currentDisplayValue = $(controlDisplay).val();
  var updateDisplayValue = toHumanReadableDate(v)
  if (currentDisplayValue == updateDisplayValue) {
    return;
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