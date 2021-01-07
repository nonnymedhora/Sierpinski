<script>
var vw = window.innerWidth || document.documentElement.clientWidth;
var gridcols = Math.max(1, Math.min(5, Math.floor((vw - 32) / 200)));
var firstr = -0.5;
var firstj = 0.0;
var firstsize = 3.0;
var orbitlength = 240;
var pixelRatio = Math.ceil(window.devicePixelRatio || 1);

var opts = document.location.search.split('&');
for (var j in opts) {
  var m;
  if (null !== (m = opts[j].match(/grid=(\d+)/))) {
    gridcols = Math.max(1, parseInt(m[1]));
  }
  if (null !== (m = opts[j].match(/c=([-+]?\d?\.?\d+)(?:([-+]\d?\.\d+)i)?/))) {
    firstr = parseFloat(m[1]);
    firstj = parseFloat(m[2] || 0);
  }
  if (null !== (m = opts[j].match(/s=([-+]?\d?\.?\d+(?:e[-+]\d+)?)/))) {
    firstsize = parseFloat(m[1]);
  }
}

var cssDims = Math.max(32,
  Math.floor((vw - 48) / gridcols) - 16);
var dims = Math.floor(cssDims * pixelRatio);
var dims2 = dims * dims;

function grid() {
  return document.getElementById('grid');
}

function ensurerow(k) {
  var rn = Math.max(2, (k - (k % gridcols)) / gridcols);
  var table = grid();
  for (var r = 0; r <= rn; ++r) {
    var row = (r >= table.rows.length) ?
      table.insertRow(table.rows.length) : table.rows[r];
    for (var c = 0; c < gridcols; ++c) {
      var cell = (c >= row.cells.length) ?
        cell = row.insertCell(c) : cell = row.cells[c];
      cell.style.height = cell.style.width = cssDims + 'px';
    }
  }
}

ensurerow(1);

grid().rows[1].cells[Math.min(1, gridcols - 1)]
  .appendChild(document.getElementById('text'));


var it = [];  // the current iteration num
var cc = [];  // the constant at every pixel
var zz = [];  // the current iterate value
var bb = [];  // the recent value at iteration r
var pp = [];  // the point at which the value was first closer than epsilon2
var nn = [];  // the number of iterations until divergence (or convergence, neg)
var un = [];  // number of uncomputed pixels
var di = [];  // number of known diverged pixels
var hi = [];  // list of [uncomputed, iter]
var ss = [];  // list of uncomputed indexes
var fi = [];  // 1 if finished.
var sizes = [];
var gridboards = 0;
var steps = 0;
var unknowncolor = "rgb(32,16,64)";

function cycle(m, z, c, p) {
  var len = figureperiod(p[m]);
  return suborbit(m, z, c, len);
}

function orbit(m, c) {
  return suborbit(m, c, c, orbitlength);
}

function suborbit(m, z, c, len) {
  var m2 = m * 2;
  var m2i = m2 + 1;
  var r = z[m2];
  var j = z[m2i];
  var cr = c[m2];
  var cj = c[m2i]
  var result = [r, j];
  while (result.length < len * 2) {
    var ra = r * r - j * j + cr;
    var j = 2 * r * j + cj;
    r = ra;
    result.push(r, j)
  }
  return result;
}

function compute(m, i, n, z, c, b, p, epsilon, epsilon2) {
  if (n[m]) return 0;
  var m2 = m * 2;
  var m2i = m2 + 1;
  var r = z[m2];
  var j = z[m2i];
  var ra = r * r - j * j + c[m2];
  var ja = 2 * r * j + c[m2i];
  var d = ra * ra + ja * ja;
  if (d > 4.0) {
    n[m] = i;
    return 1;
  }
  z[m2] = ra;
  z[m2i] = ja;
  var rb = b[m2];
  var jb = b[m2i];
  var db = Math.abs(rb - ra) + Math.abs(jb - ja);
  if (db <= epsilon2) {
    if (!p[m]) { p[m] = i; }
    if (db <= epsilon) {
      n[m] = -i;
      return -1;
    }
  }
  return 0;
}

function iterate(k) {
  var i = ++it[k];  // total iteration count
  var z = zz[k];    // current iterate for each pixel
  var c = cc[k];    // current constant for each pixel
  var n = nn[k];    // number of iterations before conv/divergence
  var b = bb[k];    // recent remembered z
  var p = pp[k];    // iteration at which converged within epsilon2
  var s = ss[k];    // speedy list of indexes to compute
  var epsilon = 1e-15;
  var epsilon2 = 1e-10;
  // head and tail factor i into an odd num and largest power of 2.
  var head = i, tail = 1;
  while ((head & 1) == 0) { head >>= 1; tail <<= 1; }
  if (s === null && un[k] <= z.length / 4) {
    var news = [];
    for (var m = 0; m < dims2; ++m) {
      if (n[m]) continue;
      news.push(m);
    }
    s = ss[k] = news;
  }
  if (s === null) {
    if (head <= tail) {
      for (var m = 0; m < dims2; ++m) {
        if (n[m]) continue;
        b[m * 2] = z[m * 2];
        b[m * 2 + 1] = z[m * 2 + 1];
        p[m] = 0;
      }
    }
    var count = 0, diverged = 0, a;
    for (var m = 0; m < dims2; ++m) {
      a = compute(m, i, n, z, c, b, p, epsilon, epsilon2);
      if (a) { count += 1; }
      if (a > 0) { diverged += 1; }
    }
    un[k] -= count;
    di[k] += diverged;
  } else {
    if (s.length > un[k] * 1.25) {
      var news = [];
      for (var t = 0; t < s.length; ++t) {
        if (n[s[t]]) continue;
        news.push(s[t]);
      }
      s = ss[k] = news;
    }
    if (head <= tail) {
      for (var t = 0; t < s.length; ++t) {
        var m = s[t];
        if (n[m]) continue;
        b[m * 2] = z[m * 2];
        b[m * 2 + 1] = z[m * 2 + 1];
        p[m] = 0;
      }
    }
    var count = 0, diverged = 0, a;
    for (var t = 0; t < s.length; ++t) {
      a = compute(s[t], i, n, z, c, b, p, epsilon, epsilon2);
      if (a) { count += 1; }
      if (a > 0) { diverged += 1; }
    }
    un[k] -= count;
    di[k] += diverged;
  }
  if (hi[k][0][0] - un[k] >= dims2 / 256) {
    hi[k].unshift([un[k], di[k], i]);
  }
  if (!fi[k] && un[k] != dims2) {
    fi[k] = i;
  }
}

function setboard(k, size, re, im) {
  while (it.length <= k) {
    it.push(0);
    fi.push(0);
    zz.push([]);
    cc.push([]);
    nn.push([]);
    bb.push([]);
    pp.push([]);
    ss.push(null);
    sizes.push([0, 0, 0]);
    un.push(dims2);
    di.push(0);
    hi.push([[dims2, 0]]);
  }
  sizes[k] = [size, re, im];
  it[k] = 1;
  var c = cc[k] = [];
  var n = nn[k] = [];
  var p = pp[k] = [];
  ss[k] = null;
  un[k] = dims2;
  di[k] = 0;
  hi[k] = [[dims2, 0, 1]];
  for (var y = 0; y < dims; y++) {
    var j = (0.5 - (y / dims)) * size + im;
    for (var x = 0; x < dims; x++) {
      var r = ((x / dims) - 0.5) * size + re;
      c.push(r, j);
      if (r * r + j * j > 4) {
        n.push(1);
        p.push(0);
        un[k] -= 1;
      } else {
        n.push(0);
        p.push(0);
      }
    }
  }
  zz[k] = c.slice();
  bb[k] = c.slice();
  fi[k] = (un[k] == dims2 ? 0 : 1);
}

function truncateboards(k) {
  if (it.length > k) {
    it.length = k;
    zz.length = k;
    cc.length = k;
    nn.length = k;
    sizes.length = k;
  }
}

function clearseq(k) {
  var ctx = overlay(k).getContext('2d');
  ctx.clearRect(0, 0, dims, dims);
}

function showseq(k, seq, color) {
  var ctx = overlay(k).getContext('2d');
  ctx.fillStyle = color;
  var sc = sizes[k];
  var s = sc[0];
  var cr = sc[1];
  var cj = sc[2];
  for (var i = 0; i < seq.length; i += 2) {
    var r = seq[i], j = seq[i+1];
    var x = (r - cr) / s * dims + dims/2;
    var y = -(j - cj) / s * dims + dims/2;
    if (x >= 0 && y >= 0 && x < dims && y < dims) {
      ctx.beginPath();
      ctx.arc(x, y, 2 * pixelRatio, 0, 2 * Math.PI, false);
      ctx.fill();
    }
  }
}

function drawcolor(k, i) {
  var color = makecolor(k, i);
  var converged = makecolor(k, -i);
  var n = nn[k];
  var ctx = board(k).getContext('2d');
  for (var m = 0; m < dims2; ++m) {
    if (n[m] == i || n[m] == -i) {
      var x = m % dims;
      var y = (m - x) / dims;
      ctx.fillStyle = n[m] > 0 ? color : converged;
      ctx.fillRect(x, y, 1, 1);
    }
  }
}

function colornum(n) {
  if (n <= 0) { return "0"; }
  if (n < 16) { return (n * 17).toString(10); }
  return "255";
}

function intcolor(n) {
  if (n <= 0) { return "0"; }
  if (n >= 1) { return "255"; }
  return (n * 255).toString(10);
}

function makecolor(k, i) {
  if (i == 0) return unknowncolor;
  if (i < 0) return 'black';
  for (var j = 0; i < hi[k][j][2]; j += 1) {}
  var frac = (dims2 - hi[k][j][0]) / dims2; // frac of pixels done.
  var len = hi[k].length, half = Math.floor((len + j) / 2);
  var s = sizes[k][0];
  if (len > 10 + j) {
    // estimate progress toward asymptote
    var slope1 = (hi[k][len-1][1] - hi[k][j][1])
               / (hi[k][len-1][2] - hi[k][j][2]),
        slope2 = (hi[k][j+3][1] - hi[k][j][1])
               / (hi[k][j+3][2] - hi[k][j][2]),
        slope3 = (hi[k][j+5][1] - hi[k][j][1])
               / (hi[k][j+5][2] - hi[k][j][2]);
    var frac2 = 1.0 - (Math.max(slope2, slope3) / slope1);
    var fl = Math.pow(frac2, 2);
    if (frac2 > 0 && fl > frac) { frac = fl; }
  }
  var ff = Math.pow(frac, 2);
  var fr = Math.pow(frac, 0.333);
  var fg = Math.pow(frac, 3);
  var g = intcolor(Math.max(fg, Math.min(fr, i * Math.pow(s, 0.33) / 64)));
  var r = intcolor(Math.min(fr, i * Math.pow(s, 0.25) / 64));
  var b = intcolor(ff / 3 + 0.667);
  return "rgb(" + r + "," + g + "," + b + ")";
}

function draw(k) {
  if (nn.length <= k) return;
  var n = nn[k];
  var ctx = board(k).getContext('2d');
  for (var m = 0; m < dims2; ++m) {
    var x = m % dims;
    var y = (m - x) / dims;
    ctx.fillStyle = makecolor(k, n[m]);
    ctx.fillRect(x, y, 1, 1);
  }
}

function drawall() {
  for (var k = 0; k < nn.length; ++k) {
    draw(k);
  }
}

function tablestring(k) {
  var factor = (3.0 / firstsize) * (Math.pow(zoomfactor, k));
  if (factor < 100000) {
    factor = +factor.toFixed(3) + 'x';
  } else {
    factor = factor.toExponential(2);
  }
  return '<canvas width=' + dims + ' height=' + dims +
         ' style="height:' + cssDims + 'px;width:' + cssDims + 'px;">' +
         '</canvas>' +
         '<div class="rect"></div>' +
         '<canvas class=overlay width=' + dims + ' height=' + dims +
         ' style="height:' + cssDims + 'px;width:' + cssDims + 'px;">' +
         '</canvas>' +
         '<a class="zoomnum" href="?grid=1">' + factor + '</a>';
}

function progress() {
  var shift = steps++;
  var k = 0;
  while (1 << k & shift) k += 1;
  k = it.length - 1 - (k % it.length);
  // Do at least 1000 pixel-steps.
  var u = un[k];
  for (var amt = 0; u > 0 && amt < 1000; amt += (u + 1)) {
    iterate(k);
    if (un[k] < u) {
      drawcolor(k, it[k]);
      u = un[k];
    }
  }
  setTimeout(progress, 0);
}

function absoluteLeft(target) {
  var left = 0;
  while (target) {
    left += target.offsetLeft;
    target = target.offsetParent;
  }
  return left;
}

function absoluteTop(target) {
  var top = 0;
  while (target) {
    top += target.offsetTop;
    target = target.offsetParent;
  }
  return top;
}

function eventtarget(e) {
  var target = (e.target ? e.target : e.srcElement ? e.srcElement : null);
  while (target) {
    if (target.id) { return target; }
    target = target.parentNode;
  }
  return null;
}

document.onmousedown = function(e) {
  var target = eventtarget(e);
  if (e.button != 0) { return true; }
  if (target) {
    var m = target.id.match(/b_(\d+)/);
    if (e.target.tagName == 'A') {
      e.target.href = bigurl(m[1]);
      return true;
    }
    if (m) {
      var se = document.scrollingElement || document.body;
      var x = (e.clientX + se.scrollLeft - absoluteLeft(target)) * pixelRatio;
      var y = (e.clientY + se.scrollTop - absoluteTop(target)) * pixelRatio;
      cellclick(parseInt(m[1]), x + y * dims );
      return false;
    }
  }
  return true;
}

document.onmousemove = function(e) {
  var target = eventtarget(e);
  if (target) {
    var m = target.id.match(/b_(\d+)/);
    if (m) {
      var se = document.scrollingElement || document.body;
      var x = (e.clientX + se.scrollLeft - absoluteLeft(target)) * pixelRatio;
      var y = (e.clientY + se.scrollTop - absoluteTop(target)) * pixelRatio;
      var k = parseInt(m[1]);
      var j = x + y * dims;
      target.setAttribute('title', '');
      if (cc[k] && j >= 0 && j * 2 < cc[k].length) {
        var title = 'c=' + formatcomplex(k, cc[k][j * 2], cc[k][j * 2 + 1]);
        var orb = orbit(j, cc[k]), cyc = [];
        if (nn[k][j]) {
          if (nn[k][j] > 0) {
            title += '\ndiverged in ' + nn[k][j];
          } else {
            title += '\nperiod ' + figureperiod(pp[k][j]);
            var cyc = cycle(j, bb[k], cc[k], pp[k]);
          }
        }
        for (var i = 0; i < sizes.length; i++) {
          clearseq(i);
          showseq(i, orb, 'yellow');
          showseq(i, cyc, 'red');
        }
        target.setAttribute('title', title);
      }
    }
  }
}

document.body.onmouseover = function(e) {
  document.body.className = 'hidemarks';
  var target = eventtarget(e);
  if (target) {
    var m = target.id.match(/b_(\d+)/);
    if (m) {
      k = parseInt(m[1]);
      if (nn.length <= k) return;
      for (var i = 0; i < gridboards; ++i) {
        board(i).parentElement.className = i < k ? '' : 'hidemarks';
      }
      document.body.className = '';
      if (e.target.tagName == 'A') {
        e.target.href = bigurl(k)
        e.target.title = ('centered at ' +
            formatcomplex(k, sizes[k][1], sizes[k][2]) + '\n'
          + Math.floor(1000 * (1.0 - un[k] / dims2))/10.0  + '% done '
          + 'after ' + it[k] + ' iters');
      }
    }
  }
}

function figureperiod(i) {
  // Reverse the computation that was done for exponential backoff.
  var head = i, tail = 1;
  while (head > tail) { head >>= 1; tail <<= 1; }
  return i - (head * tail) + 1;
}

function formatcomplex(k, re, im) {
  var pix = sizes[k][0] / dims;
  var digits = 0;
  while (pix < 1.0) {
    pix *= 10;
    digits += 1;
  }
  var rd = '' + Math.abs(re);
  var id = '' + Math.abs(im);
  var rs = re < 0.0 ? '\u2212' : '+';
  var is = im < 0.0 ? '\u2212' : '+';
  if (rd.length > digits + 2) rd = rd.substring(0, digits + 2);
  if (id.length > digits + 2) id = id.substring(0, digits + 2);
  return rs + rd + is + id + 'i';
}

function makeboard(k) {
  var table = grid();
  while (gridboards <= k) {
    var c = gridboards % gridcols;
    var r = (gridboards - c) / gridcols;
    var td = table.rows[r].cells[c];
    td.id = 'b_' + gridboards;
    td.innerHTML = tablestring(gridboards);
    gridboards += 1;
  }
}

function hideboardspast(k) {
  for (var j = 0; j <= k && j < gridboards; ++j) {
    board(j).parentElement.style.visibility = 'visible';
  }
  for (var j = k + 1; j < gridboards; ++j) {
    board(j).parentElement.style.visibility = 'hidden';
    removerect(j - 1);
  }
}

function board(k) {
  return document.getElementById("b_" + k).firstElementChild;
}

function rect(k) {
  return board(k).nextElementSibling;
}

function overlay(k) {
  return rect(k).nextElementSibling;
}

function cellclick(k, m) {
  if (k >= sizes.length) return;
  hideboardspast(k);
  ensurerow(k + 1);
  setTimeout(function() { cellclickdelay(k, m); }, 1);
}

var zoomfactor = 5;

function cellclickdelay(k, m) {
  if (k >= sizes.length) return;
  var osize = sizes[k][0];
  var ore = sizes[k][1];
  var oim = sizes[k][2];
  cx = m % dims;
  cy = (m - cx) / dims;
  hideboardspast(k + 1);
  showrect(k, cx / dims, cy / dims);
  truncateboards(k + 2);
  // Do slow parts after a moment.
  setTimeout(function() {
    nsize = osize / zoomfactor;
    nre = ore + ((cx / dims) - 0.5) * osize;
    nim = oim + (0.5 - (cy / dims)) * osize;
    setboard(k + 1, nsize, nre, nim);
    makeboard(k + 1);
    draw(k + 1);
  }, 10);
}

function showrect(k, x, y) {
  var s = rect(k).style;
  board(k).parentElement.className = '';
  s.top = (y * cssDims - cssDims / 2 / zoomfactor) + 'px';
  s.left = (x * cssDims - cssDims / 2 / zoomfactor) + 'px';
  s.width = (cssDims / zoomfactor) + 'px';
  s.height = (cssDims / zoomfactor) + 'px';
}

function bigurl(k) {
  if (k >= sizes.length) return;
  var osize = sizes[k][0];
  osize = parseFloat(osize.toPrecision(
     Math.max(2, -Math.ceil(Math.log(osize)/2.5))))
  var ore = sizes[k][1];
  var oim = sizes[k][2];
  return '?grid=1&s=' + osize + '&c=' + ore + (oim < 0 ? '' : '+') + oim + 'i'
}

function removerect(k) {
  var s = rect(k).style;
  s.top = s.left = s.width = s.height = '';
}

function start() {
  setboard(0, firstsize, firstr, firstj);
  makeboard(0);
  draw(0);
  progress();
}

if (dims > 800) {
  document.getElementById("b_0").innerHTML = '<p>Starting...';
  setTimeout(start, 100);
} else{
  setTimeout(start, 1);
}
</script>
