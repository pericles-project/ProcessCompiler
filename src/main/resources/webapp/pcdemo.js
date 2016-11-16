$(function() {

	var linkNodes = {};
	var save_to=null;

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
	
	editor.on("change", function(editor, changeEvent){
		if(save_to && changeEvent.origin != "setValue") {
			scenario.files[save_to].text = editor.getValue();
		}
	})

	var desc_node = $('#file-desc')
	
	var select_file = function(name) {
		var file = scenario.files[name];
		var linkNode = linkNodes[name];

		// Highlight file link
		$('a.file-link').removeClass('active');
		linkNode.addClass('active');

		if(typeof file.text == "string") {
			// Show editor
			$('#file-img').hide();
			editor.setValue(file.text);
			save_to = name;
			if (name.match(/\.ttl$/)) {
				editor.setOption("mode", "text/turtle");
			} else if (name.match(/\.bpmn2?$/)) {
				editor.setOption("mode", "application/xml");
			} else {
				editor.setOption("mode", "text/plain");
			}
			$(editor.getWrapperElement()).show();
		} else if(/\.(jpg|png)/.test(name)) {
			// Show image
			$(editor.getWrapperElement()).hide();
			$('#file-img').attr('src', '/demo/'+scenario.name+'/_files/'+name).show();
		}
		desc_node.text(file.desc ? file.desc : "");
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

	var names = Object.keys(scenario.files).sort();
	for (var i = 0; i < names.length; i++) {
		update(names[i]);
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
				dataType : "json"
			}).done(function(data) {
				$.each(data.files, function(key, value) {
					if (scenario.files[key] === undefined
							|| scenario.files[key].text != value.text) {
						scenario.files[key] = value;
						update(key);
					}
				});
				$('#output-file-links > a.nav-link:first').click();
			}).fail(function(response){
				if(response.responseJSON)
					alert(response.responseJSON.error.message);
				else
					alert(response.responseText);
			})
		})
	})

})