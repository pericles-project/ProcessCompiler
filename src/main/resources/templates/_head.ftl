<!DOCTYPE html>
<html lang="en">
  <head>
    <!-- Required meta tags always come first -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta http-equiv="x-ua-compatible" content="ie=edge">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.4/css/bootstrap.min.css" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.0.0/jquery.min.js" crossorigin="anonymous"></script>

    </head>
  <body>

<nav class="navbar  navbar-dark bg-primary">
  <button class="navbar-toggler hidden-sm-up" type="button" data-toggle="collapse" data-target="#exCollapsingNavbar2" aria-controls="exCollapsingNavbar2" aria-expanded="false" aria-label="Toggle navigation">
    &#9776;
  </button>
  <div class="collapse navbar-toggleable-xs" id="exCollapsingNavbar2">
    <a class="navbar-brand" href="/demo/">PERICLES - ProcessCompiler DEMO</a>
    <ul class="nav navbar-nav">
      <#list scenarios as scenario >
      <li class="nav-item">
        <a class="nav-link" href="/demo/${scenario?url}">${scenario}</a>
      </li>
      </#list>
    </ul>
  </div>
</nav>

<img src="http://images.tate.org.uk/sites/default/files/images/pericleslogosmall.jpg" style="float:right; height: 3em; margin:5px" />
<div class="container-fluid" style="margin-top:2em">