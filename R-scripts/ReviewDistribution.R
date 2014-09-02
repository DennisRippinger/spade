library(RMySQL)

con = dbConnect(dbDriver("MySQL"), user="root", password="root", dbname="Spade", host="localhost")
density = dbGetQuery(con,"SELECT rating FROM `spade`.products;")
df <- data.frame(density = as.numeric(density$rating))
df$density = round(df$density, digits = 2)
tableRating <- xtabs(df)

plot(tableRating, type="o")
dbDisconnect(con)
View(tableRating)