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
        fillProducts();
    }

    if (isChecked(byDate) && isChecked(byStore) && isChecked(byProduct)) {
        createAllReport();
    } else if (isChecked(byDate) && isChecked(byStore)) {
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
    plotName.forEach(function(value) {
        data.addColumn('number', value);
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
        var data = prepareDataByDateStore(fullDataSet, reportType, storeIds);
        var reportContent = buildChecksAggregationViews(fullDataSet);
        $("#reportTable").html(reportContent);

        drawPlot(data, stores);
    });
}

function createAllReport() {
    fetchXML(address + "/api/checks/aggregation/byDateAndStore", {since: from, limit: limit}, function (d) {
        var fullDataSet = $(d).find("checksAggregationResultRepresentation");
        var data = prepareDataByAll(fullDataSet, reportType, storeIds, productIds[0]);
        var reportContent = buildChecksAggregationViews(fullDataSet);
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
        var data = prepareDataByDateProduct(fullDataSet, reportType, productIdsEnabled);
        var reportContent = buildChecksAggregationViews(fullDataSet);
        $("#reportTable").html(reportContent);

        drawPlot(data, productsEnabled);
    });
}

function createProductStoreReport() {
    fetchXML(address + "/api/products/aggregation/byDateAndStore", {}, function (d) {
        var fullDataSet = $(d).find("productsAggregationByStoreAndDateResultRepresentation");
        var data = prepareDataByDateProduct(fullDataSet, reportType, productIds);
        var reportContent = buildChecksAggregationViews(fullDataSet);
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

function prepareDataByAll(dataList, value, stores, product) {
    var rawTs = $(dataList).map(function() {
        return Number($(this).find("timestamp").text())
    });
    var timestamps = $.unique(rawTs).map(function() {
        return new Date(this)
    });

    var list = stores.map(function (store) {
        var dataByStore = $(dataList).filter(function () {
            return Number($(this).find("storeId").text()) == store;
        });

        dataFiltered = $(dataByStore).filter(function () {
            return Number($(this).find("productId").text()) == product;
        });

        var res = dataFiltered.map(function() {
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

function fillProducts() {
    var productList = new List('products', {
        valueNames: ['name'],
        item: '<li class="list-group-item active" onclick="changeProduct(this)"><p class="name"></p></li>'
    });
    productList.clear();
    products.forEach(function (val) {
        productList.add({
            name: val
        });
    });
    fillProducts = function(){};
}

function changeProduct(elem) {
    var enabled = $(elem).hasClass("active");
    var text = $(elem).text();
    if (enabled) {
        $(elem).attr("list-group-item");

        var pos = productsEnabled.indexOf(text);
        productsEnabled.splice(pos, 1);
        productIdsEnabled.splice(pos, 1);
    } else {
        $(elem).attr("list-group-item active");

        productsEnabled.push(text);
        var pos = products.indexOf(text);
        var id = productIds[pos];
        productIdsEnabled.push(id);
    }

    createReport();
}