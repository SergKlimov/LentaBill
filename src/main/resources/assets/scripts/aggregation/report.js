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
        createDateStoreReportForecast();
    } else if (isChecked(byDate) && isChecked(byProduct)) {
        createDateProductReport();
    } else if (isChecked(byStore) && isChecked(byProduct)) {

    }
    else {
        alert("Unsupported aggregation type")
    }
}

function buildChecksAggregationViews(dataList, type, typeTime, typeSid) {
    return buildTable(dataList, type, typeTime, typeSid);
}

function fetchXML(url, params, callback) {
    $.ajax({
        url: url,
        method: "GET",
        contentType: "application/xml",
        data: params,
        success: callback
    });
}

function drawPlot(table, plotName, type) {
    data = new google.visualization.DataTable();

    data.addColumn('date', 'Date');
    plotName.each(function() {
        data.addColumn('number', this);
    });

    table.forEach(function (value) {
        data.addRow(value)
    });
    switch(type) {
        case 0:
            chart.draw(data, chart_options);
            break;
        default:
            chartForec.draw(data, chart_options);
            break;
    }
}

function createDateStoreReport() {
    fetchXML(address + "/api/checks/aggregation/byDateAndStore", {since: from, limit: limit}, function (d) {
        var fullDataSet = $(d).find("checksAggregationResultRepresentation");
        var data = prepareDataByDateStore(fullDataSet, "minCheckValue", storeIds.toArray(), 0);
        var reportContent = buildChecksAggregationViews(fullDataSet, reportType[0], reportTypeTime[0], reportTypeSid[0]);
        $("#reportTable").html(reportContent);

        drawPlot(data, stores, 0);
    });
}
function createDateStoreReportForecast() {
    fetchXML(address + "/api/checks/forecast/byDateAndStore/min", {since: fromForec, limit: limitForec}, function (d) {
        var fullDataSet = $(d).find("r");
        var data = prepareDataByDateStore(fullDataSet, "v", storeIds.toArray(), 1);
        var reportContent = buildChecksAggregationViews(fullDataSet, reportType[1], reportTypeTime[1], reportTypeSid[1]);
        $("#reportTableForec").html(reportContent);

        drawPlot(data, stores, 1);
    });
}
function createDateProductReport() {
    fetchXML(address + "/api/products/aggregation/byDateAndStore", {}, function (d) {
        var fullDataSet = $(d).find("productsAggregationByStoreAndDateResultRepresentation");
        var data = prepareDataByDateProduct(fullDataSet, "minCheckValue", productIds.toArray());
        var reportContent = buildChecksAggregationViews(fullDataSet, reportType[0], reportTypeTime[0], reportTypeSid[0]);
        $("#reportTable").html(reportContent);

        drawPlot(data, products, 0);
    });
}

function prepareDataByDateStore(dataList, value, stores, type) {
    var rawTs = $(dataList).map(function() {
        switch(type) {
            case 0:
                return Number($(this).find("timestamp").text())
            default:
                return Number($(this).find("ts").text())
        }
    });
    var timestamps = $.unique(rawTs).map(function() {
        return new Date(this)
    });

    var list = stores.map(function (store) {
        var dataByStore = $(dataList).filter(function () {
            switch(type) {
                case 0:
                    return Number($(this).find("storeId").text()) == store;
                default:
                    return Number($(this).find("sid").text()) == store;
            }
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

// TODO: Merge
function prepareDataByDateProduct(dataList, value, products) {
    var rawTs = $(dataList).map(function() {
        return Number($(this).find("timestamp").text())
    });
    var timestamps = $.unique(rawTs).map(function() {
        return new Date(this)
    });

    var list = products.map(function (product) {
        var dataByStore = $(dataList).filter(function () {
            return Number($(this).find("productId").text()) == product;
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