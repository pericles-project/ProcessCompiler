$(function() {

	console.log(scenario);
	var linkNodes = {};

	var editor = CodeMirror.fromTextArea(code, {
		lineNumbers : true,
		lineWrapping : false,
		extraKeys : {
			"Ctrl-Q" : function(cm) {
				cm.foldCode(cm.getCursor());
			},
			"F11" : function(cm) {
				cm.setOption("fullScreen", !cm.getOption("fullScreen"));
			},
			"Esc" : function(cm) {
				if (cm.getOption("fullScreen"))
					cm.setOption("fullScreen", false);
			}
		},
		foldGutter : true,
		gutters : [ "CodeMirror-linenumbers", "CodeMirror-foldgutter" ]
	});

	var desc_node = $('#file-desc')

	var select_file = function(name) {
		var file = scenario.files[name];
		var linkNode = linkNodes[name];
		editor.setValue(file.text);
		if (name.match(/\.ttl$/)) {
			editor.setOption("mode", "text/turtle");
		} else if (name.match(/\.bpmn2?$/)) {
			editor.setOption("mode", "application/xml");
		} else {
			editor.setOption("mode", "text/plain");
		}
		desc_node.text(file.desc ? file.desc : "");
		$('a.file-link').removeClass('active');
		linkNode.addClass('active');
	}

	var update = function(name) {
		var file = scenario.files[name];
		var linkNode = linkNodes[name];

		if (linkNode === undefined) {
			linkNode = linkNodes[name] = $('<a href="#"/>').click(function(key) {
				return function(e) {
					e.preventDefault();
					select_file(key);
				}
			}(name))
			.addClass('nav-link file-link').text(name)
			.appendTo(file.output ? '#output-file-links': '#input-file-links');
		}

		if (linkNode.hasClass('active')) {
			select_file(name);
		}
	}

	for ( var key in scenario.files) {
		if (scenario.files.hasOwnProperty(key)) {
			update(key);
		}
	}

	$('#input-file-links > a.nav-link:first').click();

	[ "validate", "compile", "run" ].forEach(function(action) {
		$('#action-' + action).click(function(e) {
			e.preventDefault();
			$.ajax({
				type : "POST",
				url : "/demo/" + action,
				data : JSON.stringify(scenario),
				contentType : "application/json; charset=utf-8",
				dataType : "json",
				success : function(data) {
					$.each(data.files, function(key, value) {
						if (scenario.files[key] === undefined
								|| scenario.files[key].text != value.text) {
							scenario.files[key] = value;
							update(key);
						}
					});
					$('#output-file-links > a.nav-link:first').click();
				},
				failure : function(errMsg) {
					console.log(errMsg);
				}
			})
			console.log(action);
		})
	})

})