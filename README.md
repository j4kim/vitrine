# vitrine
Revolutionnary mobile app

## Deploy the app server-side

* Copy all the php files of the directory `www` in your server
* Create a new database and import the sql files `picture.sql`, `subscribe.sql`, `user.sql` and `vitrine.sql`
* Edit the 4 `$DB_*` variables in `www/includes/db_login.php` to match your database configuration
* Edit the URLs in the file `Vitrine/app/src/main/res/values.string.xml` to match your server
* Rebuild the Android Application with the new strings values