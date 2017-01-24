<html>
<head>
  <title>Register to Vitrine</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body style="margin:10px;">
<div class="container">
  <div class="row">
    <h1>Register to Vitrine</h1>
    <form method="post" action="createUser.php">
        <div class="form-group">
          <label for="username">User name:</label>
          <input type="text" class="form-control" id="username" name="username">
        </div>
        <div class="form-group">
          <label for="email">Email: (optionnal)</label>
          <input type="email" class="form-control" id="email" name="email">
        </div>
        <div class="form-group">
          <label for="pwd">Password:</label>
          <input type="password" class="form-control" id="pwd" name="password">
        </div>
        <button type="submit" class="btn btn-default">Register</button>
    </form>
    
  </div>
</div>
</body>
</html>