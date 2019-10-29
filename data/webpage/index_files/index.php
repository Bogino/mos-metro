$(function() {

	var
		//add this class to all elements created by the script. the reason is that we call the script again on
		//window resize, and use the class to remove all the "artefacts" we created in the previous run.
		myClassName = 'imageMapHighlighterArtefacts',
		liHighlightClass = 'liHighlighting',
		specialAreaMark = 'area_mark',
		specialLiClassesMark = 'list_classes',
		specialAreaMarkFile = 'area_mark_file',
		// "2d context" attributes used for highlighting.
		areaHighLighting = {
			fillStyle: 'rgba(0,0,0,0.35)',
			strokeStyle: 'yellow',
			lineJoin: 'round',
			lineWidth: 2
		},
		//every imagemap that wants highlighting, should reside in a div of this 'class':
		hilightDivMarker = '.imageMapHighlighter',
		// specifically for wikis - redlinks tooltip adds this message
		ru = mw && mw.config && mw.config.get('wgUserLanguage') == 'ru',
		expandLegend = ru ? 'показать ссылки текстом' : 'show links as text',
		collapseLegend = ru ? 'скрыть ссылки текстом' : 'hide links as text',
		files = [];


	function drawMarker(context, areas) { // this is where the magic is done.

		function drawPoly(coords) {
			context.moveTo(coords.shift(), coords.shift());
			while (coords.length)
				context.lineTo(coords.shift(), coords.shift());
		}

		for (var i in areas) {
			var coords = areas[i].coords.split(',');
			context.beginPath();
			switch (areas[i].shape) {
				case 'rect':
					drawPoly([coords[0], coords[1], coords[0], coords[3], coords[2], coords[
						3], coords[2], coords[1]]);
					break;
				case 'circle':
					context.arc(coords[0], coords[1], coords[2], 0, Math.PI * 2);
					break; //x,y,r,startAngle,endAngle
				case 'poly':
					drawPoly(coords);
					break;
			}
			context.closePath();
			context.stroke();
			context.fill();
		}
	}

	function mouseAction(e) {
		var $this = $(this),
			activate = e.type == 'mouseover',
			caption = $this.text(),
			ol = $this.parent(),
			context = ol.data('context'),
			special = ol.data(specialAreaMark),
			specialFile = ol.data(specialAreaMarkFile); //read JSON file addition

		if (specialFile) {
			if (files[specialFile]) {
				$.extend(special, files[specialFile]);
				always(activate, caption, context, ol, special, $this);
			} else {
				$.get(mw.util.wikiScript(), {
						title: specialFile,
						action: 'raw'
					})
					.done(function(data) {
						files[specialFile] = JSON.parse(data);
						$.extend(special, files[specialFile]);
					})
					.always(function() {
						always(activate, caption, context, ol, special, $this);
					});
			}
		} else
			always(activate, caption, context, ol, special, $this);
	}

	function always(activate, caption, context, ol, special, $this) {
		$this.toggleClass(liHighlightClass, activate); // mark/unmark the list item. 

		context.clearRect(0, 0, context.canvas.width, context.canvas.height); // prepare for a new day.

		ol.find('li')
			.each(function() {
				var $li = $(this);
				var licap = $li.text();
				var param;
				if (activate && licap === caption) { // highlight!!!
					param = special && (special.hover && special.hover[licap]
						|| getblocks(special, licap)) || areaHighLighting;
				} else {
					param = special && special.nover && (special.nover[licap] || special.nover
						.default);
				}
				if (param) {
					$.extend(context, param);
					drawMarker(context, $li.data('areas'));
				}
			});
	}

	function getblocks(special, licap) {
		if (special.hoverblocks) {
			if (special.hoverblocks[licap])
				return special.hoverblocks[licap].value;
			for (var key in special.hoverblocks)
				if (special.hoverblocks[key] && special.hoverblocks[key].list.indexOf(licap) >=0 )
					return special.hoverblocks[key].value;
		}
		if (special.hover)
			return special.hover.default;
	}
	
	function handleOneMap() {
		var img = $(this),
			w = img.width(),
			h = img.height(),
			infoIcon = img.next(),
			parent = img.parent(),
			map = img.siblings('map:first'),
			dims = {
				position: 'absolute',
				width: w + 'px',
				height: h + 'px',
				border: 0,
				top: 0,
				left: 0
			},
			specialHighlight = img.closest(hilightDivMarker)
			.data(specialAreaMark),
			specialLiClasses = img.closest(hilightDivMarker)
			.data(specialLiClassesMark),
			specialHover = img.closest(hilightDivMarker)
			.data(specialAreaMarkFile);

		if (!('area', map)
			.length)
			return; //not an imagemap. inside "each" anonymous function, 'return' means "continue".

		var jcanvas = $('<canvas>', {
				'class': myClassName
			})
			.css(dims)
			.attr({
				width: w,
				height: h
			});
		var bgimg = $('<img>', {
				'class': myClassName,
				src: img.attr('src')
			})
			.css(dims); //completely inert image. this is what we see.
		var context = $.extend(jcanvas[0].getContext("2d"), areaHighLighting);

		// this is where the magic is done: prepare a sandwich of the inert bgimg at the bottom,
		// the canvas above it, and the original image on top,
		// so canvas won't steal the mouse events.
		// pack them all TIGHTLY in a newly minted "relative" div, so when page chnage
		// (other scripts adding elements, window resize etc.), canvas and imagese remain aligned.
		var div = $('<div>')
			.css({
				position: 'relative',
				width: w + 'px',
				height: h + 'px'
			});
		img.before(div); // put the div just above the image, and ...
		div.append(bgimg) // place the background image in the div
			.append(jcanvas) // and the canvas. both are "absolute", so they don't occupy space in the div
			.append(img); // now yank the original image from the window and place it on the div.
		img.fadeTo(1, 0); // make the image transparent - we see canvas and bgimg through it. 
		// the original, now transparent image is creating our mouse events

		infoIcon.css({
			position: 'relative'
		}); // set position to info icon
		var ol = $('<ol>', {
				'class': myClassName
			})
			.css({
				clear: 'both',
				margin: 0,
				listStyle: 'none',
				maxWidth: w + 'px',
				position: 'relative'
			})
			.data(specialAreaMark, specialHighlight)
			.data(specialAreaMarkFile, specialHover)
			.data('context', context);
		var oldiv = $('<div>')
			.html(ol)
			.css({
				clear: 'both',
				margin: 0,
				listStyle: 'none',
				maxWidth: w + 'px',
				position: 'relative'
			})
			.attr({
				'data-expandtext': expandLegend,
				'data-collapsetext': collapseLegend
			});

		// ol below image parent, hr below ol. original caption pushed below hr.
		parent.after($('<hr>', {
					'class': myClassName
				})
				.css('clear', 'both'))
			.after(oldiv);
		$('<hr>', {
					'class': myClassName
				})
				.css('clear', 'both')
			.insertBefore($(oldiv));
		
		var lis = {}; //collapse areas with same caption to one list item
		var someli; // select arbitrary one
		$('area', map)
			.each(function() {
				var text = this.title;
				var li = lis[text]; // title already met? use the same li
				if (!li) { //no? create a new one.
					var href = this.href;
					lis[text] = li = $('<li>', {
							'class': myClassName
						})
						.append($('<a>', {
							href: href,
							text: text
						}))
						.on('mouseover mouseout', mouseAction)
						.data('areas', [])
						.addClass(specialLiClasses && (specialLiClasses[text] ||
							specialLiClasses['default']))
						.appendTo(ol);
					if (specialLiClasses && specialLiClasses[text + ' super'])
						li.find('a')
							.addClass(specialLiClasses[text + ' super']);
				}
				li.data('areas')
					.push(this); //add the area to the li
				someli = li; // whichever - we just want one...
				$(this)
					.on('mouseover mouseout', function(e) {
						li.trigger(e);
					});
			});
		if (someli) someli.trigger('mouseout');
		oldiv.addClass('mw-collapsed')
			.makeCollapsible();
		ol.attr('style', ol.attr('style')
			.replace('none', 'disc'));
	}

	function init() {
		mw.util.addCSS('li.' + myClassName +
			'{white-space:nowrap; font-size:88.36%;}\n' + //css for li element
			'li.' + liHighlightClass + '{background-color:yellow;}\n' + //css for highlighted li element.
			'.rtl li.' + myClassName + '{float: right; margin-left: 1.5em;}\n' +
			'.ltr li.' + myClassName + '{float: left; margin-right: 1.5em;}\n' +
			hilightDivMarker + ' .mw-collapsible-toggle {float: none}');
		$(hilightDivMarker + ' img')
			.each(handleOneMap);
	}

	//has at least one "imagehighlight" div, and canvas-capable browser:
	if ($(hilightDivMarker)
		.length && $('<canvas>')[0].getContext)
		mw.loader.using(['jquery.makeCollapsible', 'mediawiki.util'])
		.done(init);
});