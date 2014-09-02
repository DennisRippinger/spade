library(RMySQL)

con = dbConnect(dbDriver("MySQL"), user="root", password="root", dbname="spade", host="localhost")
density = dbGetQuery(con,"SELECT densityFunction FROM `Spade`.stylometry;")
df <- data.frame(density = as.numeric(density$densityFunction))
df$density = round(df$density, digits = 1)
table <- xtabs(df)

plot(table, type="o")
dbDisconnect(con)
mean(df$num)
median(df$num)