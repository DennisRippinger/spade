#load libraries
library(ggplot2)
library(ggmap)
library(maps)
library(plyr)

#get wif file
#wip <- read.csv("wip.csv") 

#get map data for US counties and states
county_map <- map_data("county")
state_map <- map_data("state")

#merge wip and county_map
wip_map <- merge(county_map, wip, by.x=c("region", "subregion"), 
                 by.y=c("region","subregion"), all.x=TRUE)

#resort merged data
wip_map <- arrange(wip_map, group, order)

#relpace NA with 0's
wip_map[is.na(wip_map)] <- 0


theme_clean <- function(base_size = 12) {
  theme_grey(base_size)
    theme(
      axis.title          =   element_blank(),
      axis.text           =   element_blank(),
      panel.background    =   element_blank(),
      axis.ticks.length   =   unit(0,"cm"),
      axis.ticks.margin   =   unit(0,"cm"),
      panel.margin        =   unit(0,"lines"),
      plot.margin         =   unit(c(0,0,0,0),"lines"),
      complete            =   TRUE
    )
}

col <- terrain.colors(6)
col <- rev(col)

ggplot( wip_map, aes( x = long , y = lat , group=group ) ) +
  geom_polygon( colour = "grey" , aes( fill = factor( CATEGORY ) ) ) +
  scale_fill_manual( values =  col) +
  expand_limits( x = wip_map$long, y = wip_map$lat ) +
  coord_map( "polyconic" ) + 
  labs(fill="Number Per\nCounty") +
  theme_clean( )