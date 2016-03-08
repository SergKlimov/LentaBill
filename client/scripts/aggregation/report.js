function isChecked(checkbox) {
    return $(checkbox)[0].checked;
}

function reportButtonPressed() {
    showTable();
    createReport();
}

function createReport() {
    var byDate = $("#byDate");
    var byStore = $("#byStore");
    var byProduct = $("#byProduct");

    if (isChecked(byProduct)) {
        $("#products").show();
    }

    if (isChecked(byDate) && isChecked(byStore)) {
        createDateStoreReport();
    } else if (isChecked(byDate) && isChecked(byProduct)) {
        createDateProductReport();
    } else if (isChecked(byStore) && isChecked(byProduct)) {

    }
    else {
        alert("Unsupported aggregation type")
    }
}

function buildChecksAggregationViews(dataList) {
    return buildTable(dataList, reportType);
}

function fetchAggregationXML(url, callback) {
    $.ajax({
        url: url,
        method: "GET",
        contentType: "application/xml",
        data: {since: from, limit: limit},
        success: callback
    });
}

function drawPlot(table, plotName) {
    data = new google.visualization.DataTable();

    data.addColumn('date', 'Date');
    plotName.forEach(function(val, i) {
        data.addColumn('number', plotName[i]);
    });

    table.forEach(function (value) {
        data.addRow(value)
    });

    chart.draw(data, chart_options);
}

function createDateStoreReport() {
    fetchAggregationXML(address + "/api/checks/aggregation/byDateAndStore", function (d) {
        var fullDataSet = $(d).find("checksAggregationResultRepresentation");
        var data = prepareDataByDateStore(fullDataSet, "minCheckValue", [0, 1, 2, 3]);
        var reportContent = buildChecksAggregationViews(fullDataSet);
        $("#reportTable").html(reportContent);
        drawPlot(data, ["0", "1", "2", "3"]);
    });
}

function createDateProductReport() {
    fetchAggregationXML(address + "/api/products/aggregation/byDateAndStore", function (d) {
        var fullDataSet = $(this).find("productsAggregationByStoreAndDateResultRepresentation");
        var reportContent = buildChecksAggregationViews(fullDataSet);
        $("#reportTable").html(reportContent);
        drawPlot(fullDataSet);

        $("#ex2").slider({});
    });
}

function prepareDataByDateStore(dataList, value, stores) {
    var rawTs = $(dataList).map(function() {
        return Number($(this).find("timestamp").text())
    });
    var timestamps = $.unique(rawTs).map(function() {
        new Date(this)
    });

    var list = stores.map(function (store) {
        var dataByStore = $(dataList).filter(function () {
            return Number($(this).find("storeId").text()) == store;
        });
        var res = dataByStore.map(function() {
            return Number($(this).find(value).text())
        });
        return res;
    });
    list.unshift(timestamps); // Prepend
    var table = transpose(list);
    return table;
}

function setType(btn, type) {
    reportType = type;
    $(btn).siblings().each(function () {
        $(this).removeClass("active");
    });
    $(btn).addClass("active");

    createReport();
}