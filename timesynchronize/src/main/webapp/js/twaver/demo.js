demo = {};

demo.Util = {
    registerImage: function (url, svg) {
        var image = new Image();
        image.src = url;
        var views = arguments;
        image.onload = function () {
            twaver.Util.registerImage(demo.Util.getImageName(url), image, image.width, image.height, svg === true);
            image.onload = null;
            for (var i = 1; i < views.length; i++) {
                var view = views[i];
                if (view.invalidateElementUIs) {
                    view.invalidateElementUIs();
                }
                if (view.invalidateDisplay) {
                    view.invalidateDisplay();
                }
            }
        };
    },
    getImageName: function (url) {
        var index = url.lastIndexOf('/');
        var name = url;
        if (index >= 0) {
            name = url.substring(index + 1);
        }
        index = name.lastIndexOf('.');
        if (index >= 0) {
            name = name.substring(0, index);
        }
        return name;
    },
    demoBox: new twaver.ElementBox(),

    appendChild: function (e, parent, top, right, bottom, left) {
        e.style.position = 'absolute';
        if (left != null) e.style.left = left + 'px';
        if (top != null) e.style.top = top + 'px';
        if (right != null) e.style.right = right + 'px';
        if (bottom != null) e.style.bottom = bottom + 'px';
        parent.appendChild(e);
    },
    createNetworkToolbar: function (network, interaction) {
        var toolbar = document.createElement('div');
        demo.Util.addButton(toolbar, 'Default', 'select', function () {
            if (twaver.Util.isTouchable) {
                network.setTouchInteractions();
            } else {
                network.setDefaultInteractions();
            }
        });
        demo.Util.addButton(toolbar, 'Magnify', 'magnify', function () {
            network.setMagnifyInteractions();
        });
//        demo.Util.addButton(toolbar, 'Pan', 'pan', function () { network.setPanInteractions(); });

        demo.Util.addButton(toolbar, 'Zoom In', 'zoomIn', function () { network.zoomIn(); });
        demo.Util.addButton(toolbar, 'Zoom Out', 'zoomOut', function () { network.zoomOut(); });
        demo.Util.addButton(toolbar, 'Zoom Reset', 'zoomReset', function () { network.zoomReset(); });
        demo.Util.addButton(toolbar, 'Zoom Overview', 'zoomOverview', function () { network.zoomOverview(); });
        demo.Util.addInteractionComboBox(toolbar, network, interaction);
        demo.Util.addButton(toolbar, 'XML', 'save', function () {
            var box = network.getElementBox();
            var text = new twaver.XmlSerializer(box).serialize();
            if (twaver.Util.isIE) {
                /*
                var iframe = document.createElement('iframe');
                iframe.style.display = 'none';
                iframe.document.body = text;
                document.appendChild(iframe);
                iframe.document.execCommand("SaveAs");
                */
                var iframe = document.createElement('iframe');
                document.body.insertBefore(iframe);
                iframe.style.display = 'none';
                iframe.contentDocument.write(text);
                iframe.contentDocument.execCommand('SaveAs', true, 'file.xml');
                document.body.removeChild(iframe);
            } else {
                var uriContent = "data:text/xml," + encodeURIComponent(text);
                window.open(uriContent, 'network');
            }
            box.clear();
            new twaver.XmlSerializer(box).deserialize(text);

            text = new twaver.JsonSerializer(box).serialize();
            box.clear();
            new twaver.JsonSerializer(box).deserialize(text);

            if (console) {
                console.log(new twaver.JsonSerializer(box).serialize());
            }
        });
        demo.Util.addButton(toolbar, 'Export Image', 'export', function () {
            var canvas;
            if (network.getCanvasSize) {
                canvas = network.toCanvas(network.getCanvasSize().width, network.getCanvasSize().height);
            } else {
                canvas = network.toCanvas(network.getView().scrollWidth, network.getView().scrollHeight);
            }
            if (twaver.Util.isIE) {
                var w = window.open();
                w.document.open();
                w.document.write("<img src='" + canvas.toDataURL() + "'/>");
                w.document.close();
            } else {
                window.open(canvas.toDataURL(), 'network.png');
            }
        });
        if (demo.Util.isFullScreenSupported()) {
            demo.Util.addButton(toolbar, 'Full screen', 'fullscreen', function () {
                demo.Util.toggleFullscreen();
            });
        }
        return toolbar;
    },
    createTreeToolbar: function (tree) {
        var toolbar = document.createElement('div');
        demo.Util.addButton(toolbar, 'Reset Order', ctx+'/images/toolbar/reset.png', function () { tree.setSortFunction(null); });
        demo.Util.addButton(toolbar, 'Ascend Order', ctx+'/images/toolbar/ascend.png', function () {
            tree.setSortFunction(function (d1, d2) {
                if (d1.getName() > d2.getName()) {
                    return 1;
                } else if (d1.getName() == d2.getName()) {
                    return 0;
                } else {
                    return -1;
                }
            });
        });
        demo.Util.addButton(toolbar, 'Descend Order', ctx+'/images/toolbar/descend.png', function () {
            tree.setSortFunction(function (d1, d2) {
                if (d1.getName() < d2.getName()) {
                    return 1;
                } else if (d1.getName() == d2.getName()) {
                    return 0;
                } else {
                    return -1;
                }
            });
        });
        demo.Util.addButton(toolbar, 'Move Selection To Top', ctx+'/images/toolbar/top.png', function () { tree.getDataBox().moveSelectionToTop(); });
        demo.Util.addButton(toolbar, 'Move Selection Up', ctx+'/images/toolbar/up.png', function () { tree.getDataBox().moveSelectionUp(); });
        demo.Util.addButton(toolbar, 'Move Selection Down', ctx+'/images/toolbar/down.png', function () { tree.getDataBox().moveSelectionDown(); });
        demo.Util.addButton(toolbar, 'Move Selection To Bottom', ctx+'/images/toolbar/bottom.png', function () { tree.getDataBox().moveSelectionToBottom(); });
        demo.Util.addButton(toolbar, 'Expand', ctx+'/images/toolbar/expand.png', function () {
            if (tree.getSelectionModel().size() == 1) {
                tree.expand(tree.getSelectionModel().getLastData());
            } else {
                tree.expandAll();
            }
        });
        demo.Util.addButton(toolbar, 'Collapse', ctx+'/images/toolbar/collapse.png', function () {
            if (tree.getSelectionModel().size() == 1) {
                tree.collapse(tree.getSelectionModel().getLastData());
            } else {
                tree.collapseAll();
            }
        });
        return toolbar;
    },
    addButton: function (div, name, src, callback) {
        var button = document.createElement('input');
        button.setAttribute('type', src ? 'image' : 'button');
        button.setAttribute('title', name);
        button.style.verticalAlign = 'top';
        if (src) {
            button.style.padding = '4px 4px 4px 4px';
            if (src.indexOf('/') < 0) {
                src = ctx+'/images/toolbar/' + src + '.png';
            }
            button.setAttribute('src', src);
        } else {
            button.value = name;
        }
        button.addEventListener('click', callback, false);
        div.appendChild(button);
        return button;
    },
    addDraggableButton: function (div, name, src, className) {
        var image = new Image();
        image.setAttribute('title', name);
        image.setAttribute('draggable', 'true');
        image.style.cursor = 'move';
        image.style.verticalAlign = 'top';
        image.style.padding = '4px 4px 4px 4px';
        if (src.indexOf('/') < 0) {
            src = ctx+'/images/toolbar/' + src + '.png';
        }
        image.setAttribute('src', src);
        image.addEventListener('dragstart', function (e) {
            e.dataTransfer.effectAllowed = 'copy';
            e.dataTransfer.setData('Text', 'className:' + className);
        }, false);
        div.appendChild(image);
        return image;
    },
    addCheckBox: function (div, checked, name, callback) {
        var checkBox = document.createElement('input');
        checkBox.id = name;
        checkBox.type = 'checkbox';
        checkBox.style.padding = '4px 4px 4px 4px';
        checkBox.checked = checked;
        if (callback) checkBox.addEventListener('click', callback, false);
        div.appendChild(checkBox);
        var label = document.createElement('label');
        label.htmlFor = name;
        label.innerHTML = name;
        div.appendChild(label);
        return checkBox;
    },
    addInteractionComboBox: function (div, network, interaction) {
        var items = twaver.Util.isTouchable ? ['Touch', 'None'] :
            ['Default-Live', 'Default-Lazy', 'Edit-Live', 'Edit-Lazy', 'Magnify', 'None'];
        var callback = function () {
            if (this.value === 'Default-Live') {
                network.setDefaultInteractions();
            } else if (this.value === 'Default-Lazy') {
                network.setDefaultInteractions(true);
            } else if (this.value === 'Edit-Live') {
                network.setEditInteractions();
            } else if (this.value === 'Edit-Lazy') {
                network.setEditInteractions(true);
            } else if (this.value === 'Pan') {
                network.setPanInteractions();
            } else if (this.value === 'Magnify') {
                network.setMagnifyInteractions();
            } else if (this.value === 'Touch') {
                network.setTouchInteractions();
            } else if (this.value === 'None') {
                network.setInteractions(null);
            }
        };
        return demo.Util.addComboBox(div, items, callback, interaction);
    },
    addComboBox: function (div, items, callback, value) {
        var comboBox = document.createElement('select');
        comboBox.style.verticalAlign = 'top';
        items.forEach(function (item) {
            var option = document.createElement('option');
            option.appendChild(document.createTextNode(item));
            option.setAttribute('value', item);
            comboBox.appendChild(option);
        });

        if (callback) {
            comboBox.addEventListener('change', callback, false);
        }

        if (value) {
            comboBox.value = value;
        }
        div.appendChild(comboBox);
        return comboBox;
    },
    initOverviewPopupMenu: function (overview) {
        var popupMenu = new twaver.controls.PopupMenu(overview);
        popupMenu.setMenuItems([
            {label: 'Show Mask', type: 'check', selected: true, action: function (menuItem) {
                if (menuItem.selected) {
                    overview.setFillColor(overview.oldFillColor);
                    delete overview.oldFillColor;
                } else {
                    overview.oldFillColor = overview.getFillColor();
                    overview.setFillColor('rgba(0, 0, 0, 0)');
                }
            }},
            {label: 'Show Border', type: 'check', selected: true, action: function (menuItem) {
                if (menuItem.selected) {
                    overview.setOutlineColor(overview.oldOutlineColor);
                    delete overview.oldOutlineColor;
                } else {
                    overview.oldOutlineColor = overview.getOutlineColor();
                    overview.setOutlineColor('rgba(0, 0, 0, 0)');
                }
            }}
        ]);
    },
    addInput: function (div, value, name, callback) {
        var input = document.createElement('input');
        input.id = name;
        input.value = value;
        input.addEventListener('keydown', function (e) {
            if (e.keyCode == 13) {
                callback(input.value);
            }
        }, false);
        var label = document.createElement('label');
        label.htmlFor = name;
        label.innerHTML = name;
        div.appendChild(label);
        div.appendChild(input);
        return input;
    },
    addTab: function (tabPane, name, view, selected, closable) {
        var tab = new twaver.Tab(name);
        tab.setName(name);
        tab.setView(view);
        tabPane.getTabBox().add(tab);
        tab.setClosable(closable);
        if (selected) {
            tabPane.getTabBox().getSelectionModel().setSelection(tab);
        }
        return tab;
    },
    randomInt: function (n) {
        return Math.floor(Math.random() * n);
    },
    randomBoolean: function () {
        return demo.Util.randomInt(2) != 0;
    },
    randomNonClearedSeverity: function () {
        while (true) {
            var severity = demo.Util.randomSeverity();
            if (!twaver.AlarmSeverity.isClearedAlarmSeverity(severity)) {
                return severity;
            }
        }
        return null;
    },
    randomSeverity: function () {
        var severities = twaver.AlarmSeverity.severities;
        return severities.get(demo.Util.randomInt(severities.size()));
    },
    randomColor: function () {
        var r = demo.Util.randomInt(255);
        var g = demo.Util.randomInt(255);
        var b = demo.Util.randomInt(255);
        return '#' + demo.Util._formatNumber((r << 16) | (g << 8) | b);
    },
    randomAlarm: function (alarmID, elementID, nonClearedSeverity) {
        var alarm = new twaver.Alarm(alarmID, elementID);
        alarm.setAcked(demo.Util.randomBoolean());
        alarm.setCleared(demo.Util.randomBoolean());
        alarm.setAlarmSeverity(nonClearedSeverity ? demo.Util.randomNonClearedSeverity() : demo.Util.randomSeverity());
        alarm.setClient('raisedTime', new Date());
        return alarm;
    },
    createColor: function (rgb, a) {
        if (typeof rgb == 'string' && rgb.indexOf('#') == 0) {
            rgb = parseInt(rgb.substring(1, rgb.length), 16);
        }
        var r = (rgb >> 16) & 0xFF;
        var g = (rgb >> 8) & 0xFF;
        var b = rgb & 0xFF;
        return 'rgba(' + r + ',' + g + ',' + b + ',' + a.toFixed(3) + ')';
    },
    _formatNumber: function (value) {
        var result = value.toString(16);
        while (result.length < 6) {
            result = '0' + result;
        }
        return result;
    },
    loadXmlString: function (xml) {
        var xmlDoc;
        if (!twaver.Util.isIE && window.DOMParser) {
            var parser = new DOMParser();
            xmlDoc = parser.parseFromString(xml, 'text/xml');
        } else {
            xmlDoc = new ActiveXObject('Microsoft.XMLDOM');
            xmlDoc.async = false;
            xmlDoc.loadXML(xml);
        }
        return xmlDoc;
    },
    loadXmlFile: function (url) {
        var xhttp = new XMLHttpRequest();
        xhttp.open('GET', url, false);
        xhttp.send();
        return xhttp.responseXML;
    },
    addStyleProperty: function (box, propertyName, category, name) {
        return demo.Util._addProperty(box, propertyName, category, name, 'style');
    },
    addClientProperty: function (box, propertyName, category, name) {
        return demo.Util._addProperty(box, propertyName, category, name, 'client');
    },
    addAccessorProperty: function (box, propertyName, category, name) {
        return demo.Util._addProperty(box, propertyName, category, name, 'accessor');
    },
    _addProperty: function (box, propertyName, category, name, proprtyType) {
        var property = new twaver.Property();
        property.setCategoryName(category);
        if (!name) {
            name = demo.Util._getNameFromPropertyName(propertyName);
        }
        property.setName(name);
        property.setEditable(true);
        property.setPropertyType(proprtyType);
        property.setPropertyName(propertyName);

        var valueType;
        if (proprtyType === 'style') {
            valueType = twaver.SerializationSettings.getStyleType(propertyName);
        } else if (proprtyType === 'client') {
            valueType = twaver.SerializationSettings.getClientType(propertyName);
        } else {
            valueType = twaver.SerializationSettings.getPropertyType(propertyName);
        }
        if (valueType) {
            property.setValueType(valueType);
        }

        box.add(property);
        return property;
    },
    _getNameFromPropertyName: function (propertyName) {
        var names = propertyName.split('.');
        var name = '';
        for (var i = 0; i < names.length; i++) {
            if (names[i].length > 0) {
                name += names[i].substring(0, 1).toUpperCase() + names[i].substring(1, names[i].length);
            }
            if (i < names.length - 1) {
                name += ' ';
            }
        }
        return name;
    },
    createColumn: function (table, name, propertyName, propertyType, valueType, editable) {
        var column = new twaver.Column(name);
        column.setName(name);
        column.setPropertyName(propertyName);
        column.setPropertyType(propertyType);
        if (valueType) column.setValueType(valueType);
        column.setEditable(editable);
        column.renderHeader = function (div) {
            var span = document.createElement('span');
            span.style.whiteSpace = 'nowrap';
            span.style.verticalAlign = 'middle';
            span.style.padding = '1px 2px 1px 2px';
            span.innerHTML = column.getName() ? column.getName() : column.getPropertyName();
            span.setAttribute('title', span.innerHTML);
            span.style.font = 'bold 12px Helvetica';
            div.style.textAlign = 'center';
            div.appendChild(span);
        };
        table.getColumnBox().add(column);
        return column;
    },
    formatDate: function (date, format) {
        var o = {
            'M+': date.getMonth() + 1,
            'd+': date.getDate(),
            'h+': date.getHours(),
            'm+': date.getMinutes(),
            's+': date.getSeconds(),
            'q+': Math.floor((date.getMonth() + 3) / 3),
            'S': date.getMilliseconds()
        };
        if (/(y+)/.test(format))
            format = format.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp('(' + k + ')').test(format))
                format = format.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
        return format;
    },
    getPropertyName: function (e) {
        var name = e.property;
        if (name.indexOf('C:') == 0) {
            return name.substring(2, name.length);
        }
        if (name.indexOf('S:') == 0) {
            return name.substring(2, name.length);
        }
        return name;
    },
    align: function (elements, alignType) {
        if (!alignType) {
            throw new Error("align type can't be null");
        }
        elements = demo.Util._checkAndFilter(elements);
        if (elements == null) {
            return;
        }
        var bounds = demo.Util._getBounds(elements);
        if (bounds == null || bounds.x == Number.MAX_VALUE) {
            return;
        }
        alignType = alignType.toLowerCase();
        elements.forEach(function (node, index, array) {
            if (!(node instanceof twaver.Node)) {
                return;
            }
            var x = node.getX();
            var y = node.getY();
            switch (alignType) {
                case 'left':
                    x = bounds.x;
                    break;
                case 'right':
                    x = bounds.x + bounds.width - node.getWidth();
                    break;
                case 'top':
                    y = bounds.y;
                    break;
                case 'bottom':
                    y = bounds.y + bounds.height - node.getHeight();
                    break;
                case 'horizontalcenter':
                    x = bounds.x + (bounds.x + bounds.width - bounds.x - node.getWidth()) / 2;
                    break;
                case 'verticalcenter':
                    y = bounds.y + (bounds.y + bounds.height - bounds.y - node.getHeight()) / 2;
                    break;
            }
            node.setLocation(x, y);
        });
    },
    evenSpace: function (elements, isHorizontal, isEvenGap) {
        if (!isEvenGap) {
            isEvenGap = true;
        }
        elements = demo.Util._checkAndFilter(elements);
        if (elements == null) {
            return;
        }
        var bounds = demo.Util._getBounds(elements);
        if (bounds == null || bounds.x == Number.MAX_VALUE) {
            return;
        }
        elements.sort(function (item1, item2) {
            return isHorizontal ? (item1.getX() - item2.getX()) : (item1.getY() - item2.getY());
        });

        var count = elements.length;
        var lastItem = elements[count - 1];
        var gap;
        if (isEvenGap) {
            var realSize = 0;
            elements.forEach(function (item, index, array) {
                realSize += isHorizontal ? item.getWidth() : item.getHeight();
            });
            gap = ((isHorizontal ? bounds.width : bounds.height) - realSize) / (count - 1);
        } else {
            gap = (isHorizontal ? (bounds.width - lastItem.getWidth()) : (bounds.height - lastItem.getHeight())) / (count - 1);
        }
        var currentLocation = isHorizontal ? bounds.x : bounds.y;

        elements.forEach(function (node, index, array) {
            if (!(node instanceof twaver.Node)) {
                return;
            }
            if (isHorizontal) {
                node.setLocation(currentLocation + index * gap, node.getY());
            } else {
                node.setLocation(node.getX(), currentLocation + index * gap);
            }
            if (isEvenGap) {
                currentLocation += isHorizontal ? node.getWidth() : node.getHeight();
            }
        });
    },
    _checkAndFilter: function (elements) {
        if (!elements || elements.length == 0) {
            return null;
        }
        elements = elements.filter(function (item, index, array) {
            return item instanceof twaver.Node;
        });
        if (elements.length <= 1) {
            return null;
        }
        return elements;
    },
    _getBounds: function (elements) {
        var xMin = Number.MAX_VALUE;
        var xMax = Number.MIN_VALUE;
        var yMin = Number.MAX_VALUE;
        var yMax = Number.MIN_VALUE;

        elements.forEach(function (node, index, array) {
            if (node instanceof twaver.Node) {
                var x = node.getX();
                xMin = Math.min(x, xMin);
                var width = node.getWidth();
                xMax = Math.max(x + width, xMax);
                var y = node.getY();
                yMin = Math.min(y, yMin);
                var height = node.getHeight();
                yMax = Math.max(y + height, yMax);
            }
        });
        return { x: xMin, y: yMin, width: xMax - xMin, height: yMax - yMin };
    },
    initPropertySheet: function (sheet) {
        sheet.setEditable(true);
        var sheetBox = sheet.getPropertyBox();

        var EnumInfoFunction = function(data){
            if(data instanceof twaver.Node){
                return demo.NODE_ATTACHMENT_POSITION_TYPE;
            }else if (data instanceof twaver.Link){
                return demo.LINK_ATTACHMENT_POSITION_TYPE;
            }
        };

        var isElementVisible = function (data) {
            return data instanceof twaver.Element;
        };
        var addElementProperty = function (propertyName, propertyType, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'Basic', null, propertyType == null ? 'style' : propertyType);
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isElementVisible;
            return property;
        };
        addElementProperty('id', 'accessor', false);
        addElementProperty('name', 'accessor', true);
        addElementProperty('icon', 'accessor', true);
        addElementProperty('toolTip', 'accessor', true);
        addElementProperty('parent', 'accessor', false);
        addElementProperty('layerId', 'accessor', true);
        addElementProperty('whole.alpha');
        addElementProperty('network.label');
        addElementProperty('inner.color');

        var addLabelProperty = function (propertyName, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'Label', null, 'style');
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isElementVisible;
            return property;
        };
        addLabelProperty('label.alpha');
        addLabelProperty('label.color');
        addLabelProperty('label.font');
        addLabelProperty('label.position').setEnumInfo(EnumInfoFunction);
        addLabelProperty('label.direction').setEnumInfo(demo.ATTACHMENT_DIRECTION_TYPE);
        addLabelProperty('label.corner.radius');
        addLabelProperty('label.pointer.length');
        addLabelProperty('label.pointer.width');
        addLabelProperty('label.xoffset');
        addLabelProperty('label.yoffset');
        addLabelProperty('label.padding');
        addLabelProperty('label.padding.left');
        addLabelProperty('label.padding.right');
        addLabelProperty('label.padding.top');
        addLabelProperty('label.padding.bottom');
        addLabelProperty('label.fill');
        addLabelProperty('label.fill.color');
        addLabelProperty('label.gradient').setEnumInfo(demo.GRADIENT_TYPE);
        addLabelProperty('label.gradient.color');
        addLabelProperty('label.outline.width');
        addLabelProperty('label.outline.color');
        addLabelProperty('label.cap').setEnumInfo(demo.CAP_TYPE);
        addLabelProperty('label.join').setEnumInfo(demo.JOIN_TYPE);
        addLabelProperty('label.shadowable');

        var addSelectProperty = function (propertyName, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'Select', null, 'style');
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isElementVisible;
            return property;
        };
        addSelectProperty('select.style').setEnumInfo(demo.SELECT_TYPE);
        addSelectProperty('select.color');
        addSelectProperty('select.shape').setEnumInfo(demo.SHAPE_TYPE);
        addSelectProperty('select.width');
        addSelectProperty('select.padding');
        addSelectProperty('select.padding.left');
        addSelectProperty('select.padding.right');
        addSelectProperty('select.padding.top');
        addSelectProperty('select.padding.bottom');
        addSelectProperty('select.cap').setEnumInfo(demo.CAP_TYPE);
        addSelectProperty('select.join').setEnumInfo(demo.JOIN_TYPE);

        var addShadowProperty = function (propertyName, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'Shadow', null, 'style');
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isElementVisible;
            return property;
        };
        addShadowProperty('shadow.color');
        addShadowProperty('shadow.xoffset');
        addShadowProperty('shadow.yoffset');
        addShadowProperty('shadow.blur');

        var addAlarmProperty = function (propertyName, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'Alarm', null, 'style');
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isElementVisible;
            return property;
        };
        addAlarmProperty('alarm.alpha');
        addAlarmProperty('alarm.color');
        addAlarmProperty('alarm.font');
        addAlarmProperty('alarm.position').setEnumInfo(EnumInfoFunction);
        addAlarmProperty('alarm.direction').setEnumInfo(demo.ATTACHMENT_DIRECTION_TYPE);
        addAlarmProperty('alarm.corner.radius');
        addAlarmProperty('alarm.pointer.length');
        addAlarmProperty('alarm.pointer.width');
        addAlarmProperty('alarm.xoffset');
        addAlarmProperty('alarm.yoffset');
        addAlarmProperty('alarm.padding');
        addAlarmProperty('alarm.padding.left');
        addAlarmProperty('alarm.padding.right');
        addAlarmProperty('alarm.padding.top');
        addAlarmProperty('alarm.padding.bottom');
        addAlarmProperty('alarm.gradient').setEnumInfo(demo.GRADIENT_TYPE);
        addAlarmProperty('alarm.gradient.color');
        addAlarmProperty('alarm.outline.width');
        addAlarmProperty('alarm.outline.color');
        addAlarmProperty('alarm.cap').setEnumInfo(demo.CAP_TYPE);
        addAlarmProperty('alarm.join').setEnumInfo(demo.JOIN_TYPE);
        addAlarmProperty('alarm.shadowable');

        var addIconsProperty = function (propertyName, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'Icons', null, 'style');
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isElementVisible;
            return property;
        };
        addIconsProperty('icons.names');
        addIconsProperty('icons.colors');
        addIconsProperty('icons.position').setEnumInfo(EnumInfoFunction);
        addIconsProperty('icons.orientation').setEnumInfo(demo.ORIENTATION_TYPE);
        addIconsProperty('icons.xoffset');
        addIconsProperty('icons.yoffset');
        addIconsProperty('icons.xgap');
        addIconsProperty('icons.ygap');

        var isNodeVisible = function (data) {
            return data instanceof twaver.Node;
        };
        var addNodeProperty = function (propertyName, propertyType, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'Node', null, propertyType == null ? 'style' : propertyType);
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isNodeVisible;
            return property;
        };
        addNodeProperty('image', 'accessor', true);
        addNodeProperty('x', 'accessor', true).setValueType('number');
        addNodeProperty('y', 'accessor', true).setValueType('number');
        addNodeProperty('width', 'accessor', true);
        addNodeProperty('height', 'accessor', true);
        addNodeProperty('body.type').setEnumInfo(demo.BODY_TYPE);
        addNodeProperty('angle', 'accessor', true);

        var addImageProperty = function (propertyName, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'Image', null, 'style');
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isNodeVisible;
            return property;
        };
        addImageProperty('image.padding');
        addImageProperty('image.padding.left');
        addImageProperty('image.padding.right');
        addImageProperty('image.padding.top');
        addImageProperty('image.padding.bottom');

        var addVectorProperty = function (propertyName, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'Vector', null, 'style');
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isNodeVisible;
            return property;
        };
        addVectorProperty('vector.shape').setEnumInfo(demo.SHAPE_TYPE);
        addVectorProperty('vector.fill');
        addVectorProperty('vector.fill.color');
        addVectorProperty('vector.outline.width');
        addVectorProperty('vector.outline.pattern');
        addVectorProperty('vector.outline.color');
        addVectorProperty('vector.gradient').setEnumInfo(demo.GRADIENT_TYPE);
        addVectorProperty('vector.gradient.color');
        addVectorProperty('vector.padding');
        addVectorProperty('vector.padding.left');
        addVectorProperty('vector.padding.right');
        addVectorProperty('vector.padding.top');
        addVectorProperty('vector.padding.bottom');
        addVectorProperty('vector.cap').setEnumInfo(demo.CAP_TYPE);
        addVectorProperty('vector.join').setEnumInfo(demo.JOIN_TYPE);
        addVectorProperty('vector.deep');

        var isLinkVisible = function (data) {
            return data instanceof twaver.Link;
        };
        var addLinkProperty = function (propertyName, propertyType, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'Link', null, propertyType == null ? 'style' : propertyType);
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isLinkVisible;
            return property;
        };
        addLinkProperty('fromNode', 'accessor', false);
        addLinkProperty('toNode', 'accessor', false);
        addLinkProperty('link.color');
        addLinkProperty('link.width');
        addLinkProperty('link.cap').setEnumInfo(demo.CAP_TYPE);
        addLinkProperty('link.join').setEnumInfo(demo.JOIN_TYPE);
        addLinkProperty('link.type').setEnumInfo(demo.LINK_TYPE);
        addLinkProperty('link.pattern');
        addLinkProperty('link.extend');
        addLinkProperty('link.control.point');
        addLinkProperty('link.bundle.id');
        addLinkProperty('link.bundle.enable');
        addLinkProperty('link.bundle.expanded');
        addLinkProperty('link.bundle.independent');
        addLinkProperty('link.bundle.offset');
        addLinkProperty('link.bundle.gap');
        addLinkProperty('link.looped.gap');
        addLinkProperty('link.looped.direction').setEnumInfo(demo.DIRECTION_TYPE);
        addLinkProperty('link.looped.type').setEnumInfo(demo.LINK_LOOPED_TYPE);
        addLinkProperty('link.from.position').setEnumInfo(demo.POSITION_TYPE);
        addLinkProperty('link.from.xoffset');
        addLinkProperty('link.from.yoffset');
        addLinkProperty('link.from.at.edge');
        addLinkProperty('link.to.position').setEnumInfo(demo.POSITION_TYPE);
        addLinkProperty('link.to.xoffset');
        addLinkProperty('link.to.yoffset');
        addLinkProperty('link.to.at.edge');
        addLinkProperty('link.split.by.percent');
        addLinkProperty('link.split.percent');
        addLinkProperty('link.split.value');
        addLinkProperty('link.corner').setEnumInfo(demo.LINK_CORNER_TYPE);
        addLinkProperty('link.xradius');
        addLinkProperty('link.yradius');
        addLinkProperty('link.flow');
        addLinkProperty('link.flow.converse');
        addLinkProperty('link.flow.stepping');
        addLinkProperty('link.flow.color');

        var addLinkHandleProperty = function (propertyName, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'Link Handle', null, 'style');
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isLinkVisible;
            return property;
        };
        addLinkHandleProperty('link.handler.alpha');
        addLinkHandleProperty('link.handler.color');
        addLinkHandleProperty('link.handler.font');
        addLinkHandleProperty('link.handler.position').setEnumInfo(EnumInfoFunction);
        addLinkHandleProperty('link.handler.direction').setEnumInfo(demo.ATTACHMENT_DIRECTION_TYPE);
        addLinkHandleProperty('link.handler.corner.radius');
        addLinkHandleProperty('link.handler.pointer.length');
        addLinkHandleProperty('link.handler.pointer.width');
        addLinkHandleProperty('link.handler.xoffset');
        addLinkHandleProperty('link.handler.yoffset');
        addLinkHandleProperty('link.handler.padding');
        addLinkHandleProperty('link.handler.padding.left');
        addLinkHandleProperty('link.handler.padding.right');
        addLinkHandleProperty('link.handler.padding.top');
        addLinkHandleProperty('link.handler.padding.bottom');
        addLinkHandleProperty('link.handler.fill');
        addLinkHandleProperty('link.handler.fill.color');
        addLinkHandleProperty('link.handler.gradient').setEnumInfo(demo.GRADIENT_TYPE);
        addLinkHandleProperty('link.handler.gradient.color');
        addLinkHandleProperty('link.handler.outline.width');
        addLinkHandleProperty('link.handler.outline.color');
        addLinkHandleProperty('link.handler.cap').setEnumInfo(demo.CAP_TYPE);
        addLinkHandleProperty('link.handler.join').setEnumInfo(demo.JOIN_TYPE);
        addLinkHandleProperty('link.handler.shadowable');

        var addLinkArrowProperty = function (propertyName, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'Link Arrow', null, 'style');
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isLinkVisible;
            return property;
        };
        addLinkArrowProperty('arrow.from');
        addLinkArrowProperty('arrow.from.fill');
        addLinkArrowProperty('arrow.from.shape').setEnumInfo(demo.ARROW_SHAPE_TYPE);
        addLinkArrowProperty('arrow.from.color');
        addLinkArrowProperty('arrow.from.xoffset');
        addLinkArrowProperty('arrow.from.yoffset');
        addLinkArrowProperty('arrow.from.width');
        addLinkArrowProperty('arrow.from.height');
        addLinkArrowProperty('arrow.from.outline.color');
        addLinkArrowProperty('arrow.from.outline.width');
        addLinkArrowProperty('arrow.from.at.edge');

        addLinkArrowProperty('arrow.to');
        addLinkArrowProperty('arrow.to.fill');
        addLinkArrowProperty('arrow.to.shape').setEnumInfo(demo.ARROW_SHAPE_TYPE);
        addLinkArrowProperty('arrow.to.color');
        addLinkArrowProperty('arrow.to.xoffset');
        addLinkArrowProperty('arrow.to.yoffset');
        addLinkArrowProperty('arrow.to.width');
        addLinkArrowProperty('arrow.to.height');
        addLinkArrowProperty('arrow.to.outline.color');
        addLinkArrowProperty('arrow.to.outline.width');
        addLinkArrowProperty('arrow.to.at.edge');

        var isFollowerVisible = function (data) {
            return data instanceof twaver.Follower;
        };
        var addFollowerProperty = function (propertyName, propertyType, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'Follower', null, propertyType == null ? 'style' : propertyType);
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isFollowerVisible;
            return property;
        };
        addFollowerProperty('host', 'accessor', false);
        addFollowerProperty('follower.row.index');
        addFollowerProperty('follower.column.index');
        addFollowerProperty('follower.row.span');
        addFollowerProperty('follower.column.span');
        addFollowerProperty('follower.padding');
        addFollowerProperty('follower.padding.left');
        addFollowerProperty('follower.padding.right');
        addFollowerProperty('follower.padding.top');
        addFollowerProperty('follower.padding.bottom');

        var isGroupVisible = function (data) {
            return data instanceof twaver.Group;
        };
        var addGroupProperty = function (propertyName, propertyType, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'Group', null, propertyType == null ? 'style' : propertyType);
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isGroupVisible;
            return property;
        };
        addGroupProperty('expanded', 'accessor', true);
        addGroupProperty('group.shape').setEnumInfo(demo.SHAPE_TYPE);
        addGroupProperty('group.fill');
        addGroupProperty('group.fill.color');
        addGroupProperty('group.outline.width');
        addGroupProperty('group.outline.color');
        addGroupProperty('group.gradient').setEnumInfo(demo.GRADIENT_TYPE);
        addGroupProperty('group.gradient.color');
        addGroupProperty('group.padding');
        addGroupProperty('group.padding.left');
        addGroupProperty('group.padding.right');
        addGroupProperty('group.padding.top');
        addGroupProperty('group.padding.bottom');
        addGroupProperty('group.cap').setEnumInfo(demo.CAP_TYPE);
        addGroupProperty('group.join').setEnumInfo(demo.JOIN_TYPE);
        addGroupProperty('group.deep');

        var isGridVisible = function (data) {
            return data instanceof twaver.Grid;
        };
        var addGridProperty = function (propertyName, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'Grid', null, 'style');
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isGridVisible;
            return property;
        };
        addGridProperty('grid.row.count');
        addGridProperty('grid.column.count');
        addGridProperty('grid.row.percents');
        addGridProperty('grid.column.percents');
        addGridProperty('grid.border');
        addGridProperty('grid.border.left');
        addGridProperty('grid.border.right');
        addGridProperty('grid.border.top');
        addGridProperty('grid.border.bottom');
        addGridProperty('grid.padding');
        addGridProperty('grid.padding.left');
        addGridProperty('grid.padding.right');
        addGridProperty('grid.padding.top');
        addGridProperty('grid.padding.bottom');
        addGridProperty('grid.fill');
        addGridProperty('grid.fill.color');
        addGridProperty('grid.deep');
        addGridProperty('grid.cell.deep');

        var isShapeLinkVisible = function (data) {
            return data instanceof twaver.ShapeLink;
        };
        var addShapeLinkProperty = function (propertyName, propertyType, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'ShapeLink', null, propertyType == null ? 'style' : propertyType);
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isShapeLinkVisible;
            return property;
        };
        addShapeLinkProperty('points', 'accessor', false);
        addShapeLinkProperty('shapelink.type').setEnumInfo(demo.SHAPELINK_TYPE);

        var isShapeNodeVisible = function (data) {
            return data instanceof twaver.ShapeNode;
        };
        var addShapeNodeProperty = function (propertyName, propertyType, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'ShapeNode', null, propertyType == null ? 'style' : propertyType);
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isShapeNodeVisible;
            return property;
        };
        addShapeNodeProperty('points', 'accessor', false);
        addShapeNodeProperty('segments', 'accessor', false);
        addShapeNodeProperty('shapenode.closed');

        var isBusVisible = function (data) {
            return data instanceof twaver.Bus;
        };
        var addBusProperty = function (propertyName, editable) {
            var property = demo.Util._addProperty(sheetBox, propertyName, 'Bus', null, 'style');
            property.setEditable(editable == null ? true : editable);
            property.isVisible = isBusVisible;
            return property;
        };
        addBusProperty('bus.style').setEnumInfo(demo.BUS_STYLE_TYPE);
    },
    createDraggableNetwork: function (box) {
        var network = new Network(box);

        network.getView().addEventListener('dragover', function (e) {
            if (e.preventDefault) {
                e.preventDefault();
            } else {
                e.returnValue = false;
            }
            e.dataTransfer.dropEffect = 'copy';
            return false;
        }, false);
        network.getView().addEventListener('drop', function(e) {
            var target = network.getElementAt(e); 
            if (e.stopPropagation) {
                e.stopPropagation();
            }
            if (e.preventDefault) {
                e.preventDefault();
            } else {
                e.returnValue = false;
            }
            var text = e.dataTransfer.getData('Text');
            var imageText = e.dataTransfer.getData('Image');
            var shapeText = e.dataTransfer.getData('Shape');
            if (!text) {
                return false;
            }
            if (text && text.indexOf('className:') == 0) {
                var className = text.substr(10, text.length)
                if(className === "twaver.Node"){
                    if(imageText && imageText.indexOf('image:') == 0){
                        demo.Util._createElement(network, text.substr(10, text.length), network.getLogicalPoint(e), imageText.substr(6, imageText.length));
                    }else if(shapeText && shapeText.indexOf('shape:') == 0){
                        demo.Util._createElement(network, text.substr(10, text.length), network.getLogicalPoint(e), null, shapeText.substr(6, shapeText.length));
                    } else {
                        demo.Util._createElement(network, text.substr(10, text.length), network.getLogicalPoint(e), null, null, target);
                    }
                } else {
                    demo.Util._createElement(network, text.substr(10, text.length), network.getLogicalPoint(e), null, null, target);
                }
            }
            if (text && text.indexOf('<twaver') == 0) {
                network.getElementBox().clear();
                new twaver.XmlSerializer(network.getElementBox()).deserialize(text);
            }
            return false;
        }, false);

        network.getView().setAttribute('draggable', 'true');
        network.getView().addEventListener('dragstart', function (e) {
            e.dataTransfer.effectAllowed = 'copy';
            e.dataTransfer.setData('Text', new twaver.XmlSerializer(network.getElementBox()).serialize());
        }, false);

        return network;
    },
    _createElement: function(network, className, centerLocation, imageSrc, vectorShape, parent) {
        var element = twaver.Util.newInstance(className);
        element.setCenterLocation(centerLocation);

        if (!parent) {
            element.setParent(network.getCurrentSubNetwork());
        } else {
            element.setParent(parent);
        }

        if (imageSrc) {
            element.setImage(imageSrc);
        }
        if(vectorShape){
            element.setStyle('body.type', 'vector');
            element.setStyle('vector.shape', vectorShape);
        }
        network.getElementBox().add(element);
        network.getElementBox().getSelectionModel().setSelection(element);
    },
    isFullScreenSupported: function () {
        var docElm = document.documentElement;
        return docElm.requestFullscreen || docElm.webkitRequestFullScreen || docElm.mozRequestFullScreen;
    },
    toggleFullscreen: function () {
        if (demo.Util.isFullScreenSupported()) {
            var fullscreen = document.fullscreen || document.mozFullScreen || document.webkitIsFullScreen;
            if (!fullscreen) {
                var docElm = document.documentElement;
                if (docElm.requestFullscreen) {
                    docElm.requestFullscreen();
                } else if (docElm.webkitRequestFullScreen) {
                    docElm.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT);
                } else if (docElm.mozRequestFullScreen) {
                    docElm.mozRequestFullScreen();
                }
            } else {
                if (document.exitFullscreen) {
                    document.exitFullscreen();
                } else if (document.mozCancelFullScreen) {
                    document.mozCancelFullScreen();
                } else if (document.webkitCancelFullScreen) {
                    document.webkitCancelFullScreen();
                }
            }
        }
    }
};
demo.SHAPE_TYPE = ['rectangle', 'oval', 'roundrect', 'star', 'triangle', 'circle', 'hexagon', 'pentagon', 'diamond'];
demo.GRADIENT_TYPE = ['linear.east', 'linear.north', 'linear.northeast', 'linear.northwest', 'linear.south', 'linear.southeast', 'linear.southwest', 'linear.west', 'none', 'radial.center', 'radial.east', 'radial.north', 'radial.northeast', 'radial.northwest', 'radial.south', 'radial.southeast', 'radial.southwest', 'radial.west', 'spread.antidiagonal', 'spread.diagonal', 'spread.east', 'spread.horizontal', 'spread.north', 'spread.south', 'spread.vertical', 'spread.west'];
demo.DIRECTION_TYPE = ['northwest', 'north', 'northeast', 'east', 'west', 'south', 'southwest', 'southeast'];
demo.ATTACHMENT_DIRECTION_TYPE = ['aboveleft', 'aboveright', 'belowleft', 'belowright', 'leftabove', 'leftbelow', 'rightabove', 'rightbelow', 'above', 'below', 'left', 'right'];
demo.POSITION_TYPE = ['topleft.topleft', 'top.top', 'topright.topright', 'right.right', 'left.left', 'bottom.bottom', 'bottomleft.bottomleft', 'bottomright.bottomright'];
demo.NODE_ATTACHMENT_POSITION_TYPE = ['hotspot',
    'topleft.topleft',
    'topleft.topright',
    'top.top',
    'topright.topleft',
    'topright.topright',
    'topleft',
    'top',
    'topright',
    'topleft.bottomleft',
    'topleft.bottomright',
    'top.bottom',
    'topright.bottomleft',
    'topright.bottomright',
    'left.left',
    'left',
    'left.right',
    'center',
    'right.left',
    'right',
    'right.right',
    'bottomleft.topleft',
    'bottomleft.topright',
    'bottom.top',
    'bottomright.topleft',
    'bottomright.topright',
    'bottomleft',
    'bottom',
    'bottomright',
    'bottomleft.bottomleft',
    'bottomleft.bottomright',
    'bottom.bottom',
    'bottomright.bottomleft',
    'bottomright.bottomright'];
demo.LINK_ATTACHMENT_POSITION_TYPE = ['hotspot',
    'from',
    'to',
    'topleft.topleft',
    'topleft.topright',
    'top.top',
    'topright.topleft',
    'topright.topright',
    'topleft',
    'top',
    'topright',
    'topleft.bottomleft',
    'topleft.bottomright',
    'top.bottom',
    'topright.bottomleft',
    'topright.bottomright',
    'left.left',
    'left',
    'left.right',
    'center',
    'right.left',
    'right',
    'right.right',
    'bottomleft.topleft',
    'bottomleft.topright',
    'bottom.top',
    'bottomright.topleft',
    'bottomright.topright',
    'bottomleft',
    'bottom',
    'bottomright',
    'bottomleft.bottomleft',
    'bottomleft.bottomright',
    'bottom.bottom',
    'bottomright.bottomleft',
    'bottomright.bottomright'];
demo.LINK_TYPE = ['arc', 'triangle', 'parallel', 'flexional', 'flexional.horizontal', 'flexional.vertical', 'orthogonal', , 'orthogonal.horizontal', 'orthogonal.vertical', 'orthogonal.H.V', 'orthogonal.V.H', 'extend.top', 'extend.left', 'extend.bottom', 'extend.right'];
demo.LINK_LOOPED_TYPE = ['arc', 'rectangle'];
demo.LINK_CORNER_TYPE = ['none', 'round', 'bevel'];
demo.LAYOUT_TYPE = ['round', 'topbottom', 'bottomtop', 'symmetry', 'rightleft', 'leftright', 'hierarchic'];
demo.BUS_STYLE_TYPE = ['nearby', 'north', 'south', 'west', 'east'];
demo.SHAPELINK_TYPE = ['lineto', 'quadto', 'cubicto'];
demo.BODY_TYPE = ['none', 'default', 'vector', 'default.vector', 'vector.default'];
demo.SEGMENT_TYPE = ['moveto', 'lineto', 'quadto', 'cubicto'];
demo.CAP_TYPE = ['butt', 'round', 'square'];
demo.JOIN_TYPE = ['miter', 'round', 'bevel'];
demo.ORIENTATION_TYPE = ['top', 'left', 'bottom', 'right'];
demo.SELECT_TYPE = ['none', 'shadow', 'border'];
demo.ARROW_SHAPE_TYPE = ['arrow.standard', 'arrow.delta', 'arrow.diamond', 'arrow.short', 'arrow.slant','arrow.doubledelta','arrow.tee','arrow.box','arrow.dot','arrow.tail'];
