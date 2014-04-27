library(RMySQL)

con = dbConnect(dbDriver("MySQL"), user="root", password="root", dbname="Amazon", host="localhost")
density = dbGetQuery(con,"SELECT densityRelation FROM `Amazon`.Reviews;")
df <- data.frame(density = as.numeric(density$densityRelation))
df$density = round(df$density, digits = 1)
table <- xtabs(df)

plot(table, type="o")
dbDisconnect(con)
mean(df$num)
median(df$num)