// A Javascript Julia set generator
// Mark McClure - April/May 2015


// The main function
// Sets some parameters, calls draw_mandelbrot_set,
// and binds some functions and events.

//var main = function () {
function main() {
    "use strict";
    
    var canvas_size = 500;
    var c_update_flag = true;
    var c_main;
    var mandel_min_re = -2.1;
    var mandel_max_re = 0.5;
    var mandel_min_im = -1.3;
    var mandel_max_im = 1.3;
	var mandel_canvas = $(".mandel-canvas");
	mandel_canvas.attr('width', canvas_size);
	mandel_canvas.attr('height', canvas_size);
	draw_mandelbrot_set(mandel_canvas[0], 
		mandel_min_re, mandel_max_re,
		mandel_min_im, mandel_max_im
	);
	var julia_canvas = $(".julia-canvas");
	var julia_context = julia_canvas[0].getContext("2d");
	julia_canvas.attr('width', canvas_size);
	julia_canvas.attr('height', canvas_size);

    var input = $("#c-input");
    input.val("");
    input.keypress(function(event) {
    	if(event.keyCode == 13) {
    		var c = math.complex(input.val());
			var pos = complex_to_canvas(c, 
				mandel_min_re, mandel_max_re, 
				mandel_min_im, mandel_max_im, 
				mandel_canvas[0]
			);
			var context = mandel_canvas[0].getContext("2d");
			context.fillStyle = "#d70";
			context.fillRect(pos[0]+1,pos[1]-1,3,3);
			c_main = c;
	  		c_update_flag = false;
			var julia_set = new QuadraticJuliaSet(c, julia_canvas[0]);
	  		var algorithm = $("input:radio[name=algorithm]:checked").val();
			var julia_set = new QuadraticJuliaSet(c, julia_canvas[0]);
			julia_set.generate(algorithm);
    	}
    });
	$(".mandel-canvas").bind('click', function(event) {
		var pos = get_mouse_position(event, mandel_canvas);
		var c = canvas_to_complex(pos, 
			mandel_min_re, mandel_max_re, 
			mandel_min_im, mandel_max_im,
			mandel_canvas[0]
		);
		var context = mandel_canvas[0].getContext("2d");
		context.fillStyle = "#d70";
		context.fillRect(pos[0]+1,pos[1]-1,3,3);
		c_main = c;
		input.val(c.toString());
	  	c_update_flag = false;
		var algorithm = $("input:radio[name=algorithm]:checked").val();
		var julia_set = new QuadraticJuliaSet(c, julia_canvas[0]);
		julia_set.generate(algorithm);
	}).bind('mousemove', 
	  function(event) {
	  	if(c_update_flag == true) {
			var ijpos = get_mouse_position(event, mandel_canvas);
			var c = canvas_to_complex(ijpos, 
				mandel_min_re, mandel_max_re, 
				mandel_min_im, mandel_max_im,
				mandel_canvas[0]
			);
			input.val(c.toString())
	    }
	  });
	  $(".julia-canvas").bind('click', function(event) {
	  	var pos = get_mouse_position(event, julia_canvas);
	  	var z0 = canvas_to_complex(pos, -2,2, -2,2, julia_canvas[0]);
	  	draw_orbit(c_main, z0, julia_canvas[0]);
	  	});
	  $("#clear-button").bind('click', function() {
	  	julia_context.clearRect(0,0, julia_canvas[0].width, julia_canvas[0].height);
		draw_mandelbrot_set(mandel_canvas[0], 
			mandel_min_re, mandel_max_re,
			mandel_min_im, mandel_max_im
		);
	  	c_update_flag = true;
	  });
	  $("#download-button").bind('click', function() {
	    window.location = julia_canvas[0].toDataURL('image/png');	  	
	  });
	  $("input:radio[name=algorithm]").click(function() {
    	var value = $(this).val();
	  });
      var radios = $("input:radio[name=algorithm]")
	  	.filter('[value=inverse_iteration]')
	  	.prop('checked', true);
};


// Some convenience functions to translate mouse positions and 
// canvas coordinates to complex coordinates
function get_mouse_position(event, canvas) {
	"use strict";
	var bb = canvas[0].getBoundingClientRect();
	var i = (event.clientX-bb.left)*(canvas[0].width/bb.width);
	var j = (event.clientY-bb.top)*(canvas[0].height/bb.height);
    var pos = [i,j];
	return pos;
}
function canvas_to_xy(ij, xmin, xmax, ymin, ymax, canvas) {
	"use strict";
	return [
		((xmax-xmin)/(canvas.width-1))*ij[0]+xmin, 
		((ymin-ymax)/(canvas.height-1))*ij[1]+ymax
	]
}
function canvas_to_complex(ij, xmin, xmax, ymin, ymax, canvas) {
	"use strict";
	var xy = canvas_to_xy(ij, xmin, xmax, ymin, ymax, canvas);
	return math.complex(xy[0], xy[1])
}
function xy_to_canvas(x, y, xmin, xmax, ymin, ymax, canvas) {
	"use strict";
	return [
		Math.min(Math.floor((x-xmin)*canvas.width/(xmax-xmin)), canvas.width-1),
		Math.min(Math.floor((ymax-y)*canvas.height/(ymax-ymin)), canvas.height-1)
	]
}
function complex_to_canvas(z, xmin, xmax, ymin, ymax, canvas) {
	"use strict";
	return xy_to_canvas(z.re, z.im, xmin, xmax, ymin, ymax, canvas)
}


// Functions to compute and draw the image of the mandelbrot set.
function mandelbrot_iteration_count(x0,y0, bail) {
	"use strict";
	var x = x0, y = y0;
	var xtemp, ytemp;
	var cnt = 0;
	while(x*x+y*y <= 4 && cnt++ < bail) {
		xtemp = x; ytemp = y;
		x = xtemp*xtemp-ytemp*ytemp+x0;
		y = 2*xtemp*ytemp+y0
	}
	return cnt;
}
function draw_mandelbrot_set(canvas, xmin, xmax, ymin, ymax) {
	"use strict";
	var bail = 100;
	var mandel_context = canvas.getContext("2d");
	var canvasData = mandel_context.createImageData(canvas.width, canvas.height);
	for(var i = 0; i < canvas.width; i++) {
		for(var j = 0; j < canvas.height; j++) { 
			var c = canvas_to_xy([i,j], xmin, xmax, ymin, ymax, canvas);
			var it_cnt = mandelbrot_iteration_count(c[0],c[1], bail);
			var scaled_it_cnt = 255-255*it_cnt/(bail+1);
			var idx = (i+j*canvas.width) * 4;
			canvasData.data[idx + 0] = scaled_it_cnt; // 255*customColor(scaled_it_cnt,"r");
			canvasData.data[idx + 1] = scaled_it_cnt; // 255*customColor(scaled_it_cnt,"g");
			canvasData.data[idx + 2] = scaled_it_cnt; // 255*customColor(scaled_it_cnt,"b");
			canvasData.data[idx + 3] = 255;
		}
	}
    mandel_context.putImageData(canvasData, 0, 0);
};



// A class to hold the Julia set
// There's just one method - QuadraticJuliaSet.generate that
// draws the Julia set using either an inverse iteration 
// algorithm or an escape time algorithm.
function QuadraticJuliaSet(c, draw_canvas) { 
	this.c = c;
	this.draw_canvas = draw_canvas;
}
QuadraticJuliaSet.prototype = {
	generate: function(algorithm) {
		"use strict";
		if(algorithm == "inverse_iteration") {
			draw_inverse_iterates(this.c, this.draw_canvas);
		}
		else if(algorithm = "escape_time") {
			draw_julia_set(this.c, this.draw_canvas)
		}
	}
}

// Draw the Julia set using an escape time algorithm
function draw_julia_set(c, canvas) {
	"use strict";
	var xmin = -2, xmax = 2;
	var ymin = -2, ymax = 2;
	var bail = 100;
	var context = canvas.getContext("2d");
	var canvasData = context.createImageData(canvas.width, canvas.height);

	for(var i = 0; i < canvas.width; i++) {
		for(var j = 0; j < canvas.height; j++) { 
			var xy = canvas_to_xy([i,j], xmin, xmax, ymin, ymax, canvas);
			var it_cnt = julia_iteration_count(c.re,c.im,xy[0],xy[1]);
			var color = 255-255 * it_cnt/(bail+1);
			var idx = (i+j*canvas.width) * 4;
			canvasData.data[idx + 0] = color;
			canvasData.data[idx + 1] = color;
			canvasData.data[idx + 2] = color;
			canvasData.data[idx + 3] = 255;
		}
	}
    context.putImageData(canvasData, 0, 0);
}
function julia_iteration_count(cre,cim,x0,y0) {
	"use strict";
	var x = x0, y = y0;
	var xtemp, ytemp;
	var cnt = 0;
	while(x*x+y*y <= 4 && cnt++ < 100) {
		xtemp = x; ytemp = y;
		x = xtemp*xtemp-ytemp*ytemp+cre;
		y = 2*xtemp*ytemp+cim
	}
	return cnt;
}


// Draw the Julia set using an inverse iteration algorithm
function draw_inverse_iterates(c, draw_canvas) {
	"use strict";
	var bailout = 10;
	var draw_context = draw_canvas.getContext("2d");
	var plot_record = new Array(draw_canvas.height);
	for(var cnt=0; cnt<draw_canvas.height; cnt++) {
		plot_record[cnt] = new Array(draw_canvas.width);
	}
	for(var row = 0; row < draw_canvas.height; row++) {
		for(var col = 0; col < draw_canvas.width; col++) { 
			plot_record[row][col] = 0;
		}
	}
	var z0 = math.complex(0.12,0.34);
	for(var cnt = 0; cnt<5; cnt++) {
		z0 = math.sqrt(math.add(z0,math.multiply(c,-1)));
		z0 = math.multiply(math.sqrt(math.add(z0,math.multiply(c,-1))),-1);
	}
	var zNode = new JuliaTreeNode(z0);
	var queue = [zNode];
	var cnt = 0;
	while(queue.length > 0) {
		zNode = queue.pop();
		var z = zNode.z;
		var ij = complex_to_canvas(z, -2,2, -2,2, draw_canvas);
		var i = ij[0];
		var j = ij[1];
		if(plot_record[i][j] < bailout) {
			draw_context.fillRect(i,j,1,1);
			var z1 = math.sqrt(math.add(z,math.multiply(c,-1)));
			var z2 = math.multiply(z1,-1);
			var left = new JuliaTreeNode(z1);
			var right = new JuliaTreeNode(z2);
			zNode.left = left;
			zNode.right = right;
			queue.push(left);
			queue.push(right);
			plot_record[i][j] = plot_record[i][j]+1;
		}
	}
}

// The inverse iteration algorithm constructs the Julia set as a
// binary tree with these types of nodes.
function JuliaTreeNode(z /* , left, right */) {
	this.z = z || 0;
	this.left = "empty";
	this.right = "empty";
}
JuliaTreeNode.prototype = {
	toString: function() {
		return this.left.toString() + "<-" + this.z.toString() + "->" + this.right.toString();
	}
}

// When the Julia set canvas is clicked, we draw an orbit on top of it.
function draw_orbit(c,z0, canv) {
	"use strict";
	var z = z0;
	var cnt = 0;
	var context = canv.getContext("2d");
	var ij = complex_to_canvas(z, -2,2, -2,2, canv);

	context.beginPath();
	context.lineWidth = 2;
	context.strokeStyle = "#d00";
	context.globalAlpha = 0.3;
	context.moveTo(ij[0],ij[1]);
	while(cnt++ < 1000 && math.add(math.multiply(z.re,z.re),math.multiply(z.im,z.im)) <= 8) {
		z = math.add(math.multiply(z,z),c);
		ij = complex_to_canvas(z, -2,2, -2,2, canv);
		context.lineTo(ij[0],ij[1]);
	}
	context.stroke();
	context.globalAlpha = 1;
}

// We are ready!!
$(document).ready(main);
