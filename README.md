Unsupervised-Sentiment-Analysis
===============================
There are two projects in this repository
* loader
* unsupervised-sentiment-analysis

The dependencies and resources required by the unsupervised-sentiment-analysis project are present [here](https://www.dropbox.com/sh/hh80s8sp22ngl64/AABEBLILA8u-PkTi7h_tx-Dwa)

### Steps
===============================

* Go inside the loader directory and then execute the `start_loader` script in the background.

```
> cd loader
> ./start_loader.sh &
```

* Download the directory present [here](https://www.dropbox.com/sh/hh80s8sp22ngl64/AABEBLILA8u-PkTi7h_tx-Dwa)

* After doing this, copy the `libs` folder to the `unsupervised-sentiment-analysis/WebContent/WEB-INF/` directory of the project

```
> cp -r libs  unsupervised-sentiment-analysis/WebContent/WEB-INF/
```

* Then copy the `classes` folder to `unsupervised-sentiment-analysis/WebContent/WEB-INF/`

```
> cp -r classes  unsupervised-sentiment-analysis/WebContent/WEB-INF/
```

* You will now need to import the unldb3_0.sql databse present inside the `Database` directory. Before doing that first create a database by that name

```
> mysql -u username -p
> create database unldb3_0
> exit
> mysql -u username -p unldb3_0 < Database/unldb3_0.sql
```

* Now, go to the `unsupervised-sentiment-analysis/WebContent/WEB-INF/classes/properties` folder and edit the username and password in the `UnlEnco.properties` file to match your mysql settings. 

* After doing this, import the `unsupervised-sentiment-analysis` project into eclipse. This is a web application project. Run the project on the apache-tomcat server
