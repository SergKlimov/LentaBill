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

function drawPlot(dataList) {
    data = new google.visualization.DataTable();

    data.addColumn('date', 'Date');
    data.addColumn('number', 'Check value');

    var dataForStore = $(dataList).filter(function () {
        return Number($(this).find("storeId").text()) == 0;
    });

    dataForStore.each(function () {
        var timestamp = Number($(this).find("timestamp").text());
        var value = Number($(this).find("minCheckValue").text());
        data.addRow([new Date(timestamp), value])
    });

    chart.draw(data, chart_options);
}

function createDateStoreReport() {
    fetchAggregationXML(address + "/api/checks/aggregation/byDateAndStore", function (d) {
        var fullDataSet = $(d).find("checksAggregationResultRepresentation");
        prepareDataByDateStore(fullDataSet, "minCheckValue", [0, 1, 2, 3]);
        var reportContent = buildChecksAggregationViews(fullDataSet);
        $("#reportTable").html(reportContent);
        drawPlot(fullDataSet);

        $("#ex2").slider({});
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
    var timestamps = $.unique(rawTs);

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