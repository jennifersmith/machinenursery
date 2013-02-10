# Exploratory Data Analysis
# What questions can we ask of the data to get a rough feel for it?
setwd("~/code/machine-learning/machinenursery")

titanic <- read.csv("./titanic-data/train.csv")

# Can you obviously see that one socio economic class died more?
plot(titanic$survived, titanic$pclass, pch=19, col="blue", cex=0.5)
smoothScatter(titanic$survived, titanic$pclass)

cor(titanic$survived, titanic$pclass)

# You can roughly see that more people in the lower socio economic class (3) died
# than in the other classes but it doesn't look that significant

# How many of each socio economic class were on the ship?
# How many of each socio economic class survived? 
survived <- function(class) {
  peopleInThatClass <- titanic[titanic$pclass == class,]
  survived <- peopleInThatClass[peopleInThatClass$survived == 1,]  
  nrow(survived)
}

survivedBySex <- function(class, sex) {
  peopleInThatClass <- titanic[titanic$pclass == class,]  
  survived <- peopleInThatClass[peopleInThatClass$survived == 1 & peopleInThatClass$sex == sex,]
  nrow(survived)
}

passengers <- function(class) {
  peopleInThatClass <- titanic[titanic$pclass == class,]
  nrow(peopleInThatClass)
}

survivalSummary <- function(classes) {
  summary<-NULL
  for(class in classes) {
    numberSurvived <- survived(class)
    maleSurvived <- survivedBySex(class, "male")
    femaleSurvived <- survivedBySex(class, "female")
    total <- passengers(class)
    percentageSurvived <- numberSurvived / total
    rbind(summary,data.frame(class=class, survived=numberSurvived, total=total, percentSurvived=percentageSurvived, maleSurvived=maleSurvived, femaleSurvived=femaleSurvived))->summary
  }
  
  summary[with(summary, order(class)),]  
}

classes <- unique(titanic$pclass)
survivalSummary(classes)

# More people in the higher socio economic class survived but by no means did
# all of them survive

# Was there a greater chance of women or children within those classes surviving?

# Is there some correlation between people dying and their age? Would imagine that
# very young children have a greater chance of survival

# This fails because we don't have the age of all the people
cor(titanic$survived, titanic$age)

library(Hmisc)
ageGroups <- cut2(titanic$age, g=5)
