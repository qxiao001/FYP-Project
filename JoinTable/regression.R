#install.packages("RPostgreSQL")
require("RPostgreSQL")

# create a connection
# save the password that we can "hide" it as best as we can by collapsing it
pw <- {
  "ev093qer"
}

# loads the PostgreSQL driver
drv <- dbDriver("PostgreSQL")
# creates a connection to the postgres database
# note that "con" will be used later in each connection to the database
con <- dbConnect(drv, dbname = "postgres",
                 host = "132.147.88.190", port = 5433,
                 user = "ecoadm", password = pw)
rm(pw) # removes the password

# check for the cartable
# dbExistsTable(con, "taiwan_rain")
# TRUE

### query for table 
library(DAAG)
library(boot)
library(MASS)
df_postgres <- dbGetQuery(con, "select hourstamp,avg(conflow1) as conflow1 ,avg(conflow2) as conflow2 ,avg(conin1) as conin1, avg(conin2) as conin2, avg(conout1) as conout1 , avg(conout2) as conout2,avg(evaflow1) as evaflow1, avg(evaflow2) as evaflow2, avg(evain1) as evain1, avg(evain2) as evain2, avg(evaout1) as evaout1, avg(evaout2) as evaout2, avg(temp) as temp, avg(now) as rain,(max(consumptiontotal)-min(consumptiontotal)) as onehrconsump,avg(powertotal) as power from \"chiller_cbpt2\" group by hourstamp")

bound <- floor((nrow(df_postgres)/5)*3)
df_postgres.train <- df_postgres[1:bound, ]  
df_postgres.test <- df_postgres[(bound+1):nrow(df_postgres), ]
frml <- power~ conflow1+conflow2+conin1+conin2+conout1+conout2+evaflow1+evaflow2+evain1+evain2+evaout1+evaout2+temp+rain
fit <- lm(frml, data=df_postgres.train)
fit_r <- rlm(frml, data=df_postgres.train)
fit_g <- glm(frml, data=df_postgres.train,family=poisson)

##test using cross validation
#cv.lm(data=df_postgres.test, fit,m=3)
#cv.glm(data=df_postgres.test,fit_g,K=3)
#layout(matrix(c(1,2,3,4),2,2)) # optional 4 graphs/page 
#plot(fit)

##try polynomial regression
fit2 = lm(power~poly(conflow1,2)+poly(conflow2,2)+poly(conin1,2)+poly(conin2,2)+poly(conout1,2)+poly(conout2,2)+poly(evaflow1,2)+poly(evaflow2,2)+poly(evain1,2)+poly(evain2,2)+poly(evaout1,2)+poly(evaout2,2)+poly(temp,2)+poly(rain,2),data=df_postgres.train)
fit3 <- lm(power~poly(conflow1,3)+poly(conflow2,3)+poly(conin1,3)+poly(conin2,3)+poly(conout1,3)+poly(conout2,3)+poly(evaflow1,3)+poly(evaflow2,3)+poly(evain1,3)+poly(evain2,3)+poly(evaout1,3)+poly(evaout2,3)+poly(temp,3)+poly(rain,3),data=df_postgres.train)
fit4 = lm(power~poly(conflow1,4)+poly(conflow2,4)+poly(conin1,4)+poly(conin2,4)+poly(conout1,4)+poly(conout2,4)+poly(evaflow1,4)+poly(evaflow2,4)+poly(evain1,4)+poly(evain2,4)+poly(evaout1,4)+poly(evaout2,4)+poly(temp,4)+poly(rain,4),data=df_postgres.train)
fit5 = lm(power~poly(conflow1,5)+poly(conflow2,5)+poly(conin1,5)+poly(conin2,5)+poly(conout1,5)+poly(conout2,5)+poly(evaflow1,5)+poly(evaflow2,5)+poly(evain1,5)+poly(evain2,5)+poly(evaout1,5)+poly(evaout2,5)+poly(temp,5)+poly(rain,5),data=df_postgres.train)
anova(fit,fit2,fit3,fit4,fit5)
frml4=power~ conflow1 + I(conflow1^2) +I(conflow1^3)+I(conflow1^4)+conflow2 + I(conflow2^2) +I(conflow2^3)+I(conflow2^4)+conin1+I(conin1^2)+I(conin1^3)+I(conin1^4)+conin2+I(conin2^2)+I(conin2^3)+I(conin2^4)+conout1+I(conout1^2)+I(conout1^3)+I(conout1^4)+conout2+I(conout2^2)+I(conout2^3)+I(conout2^4)+evaflow1+I(evaflow1^2) +I(evaflow1^3)+I(evaflow1^4)+evaflow2 + I(evaflow2^2) +I(evaflow2^3)+I(evaflow2^4)+evain1+I(evain1^2)+I(evain1^3)+I(evain1^4)+evain2+I(evain2^2)+I(evain2^3)+I(evain2^4)+evaout1+I(evaout1^2)+I(evaout1^3)+I(evaout1^4)+evaout2+I(evaout2^2)+I(evaout2^3)+I(evaout2^4)
fit4_sp=lm(frml4,data=df_postgres.train)

fit4_sp_step=stepAIC(fit4_sp,direction="both")
anova(fit4,fit4_sp_step)

## test using testing set's abosolute mean error
pred4=predict(fit4_sp_step,newdata=df_postgres.test,se=TRUE)
pred=predict(fit,newdata=df_postgres.test,se=TRUE)

##se.bands=cbind(pred4$fit +2*pred4$se.fit,pred4$fit -2*pred4$se.fit)
actualvspred=cbind(df_postgres.test$power,pred$fit)
actualvspred4=cbind(df_postgres.test$power,pred4$fit)
#abs=sum(abs(actualvspred[,2]-actualvspred[,1]))/202
#abs2=sum(abs(actualvspred4[,2]-actualvspred4[,1]))/202

# view of prediction with regresssion
plot(actualvspred4[,1],actualvspred4[,2],main="predicted against actual for degree 4 polynomial step regression",xlab="actual",ylab="predicted",asp=1, pty='s')
abline(0, 1)
## another view of prediction with time
time = c(1:nrow(actualvspred4))
plot(time,actualvspred4[,1],col="blue",main="predicted against actual for degree 4 polynomial step regression",xlab="time",ylab="power",type='l')
lines(time,actualvspred4[,2],col="red")

## write back predctions 
predall <- predict(fit4_sp_step,newdata=df_postgres,se=TRUE)
results <- cbind(df_postgres,predall$fit)
results$`predall$fit` <- abs(results$`predall$fit`)
results <- results[order(results$hourstamp),]

time = c(1:nrow(results))
plot(time,results$power,col="blue",main="predicted against actual ",xlab="time",ylab="power",type='l')
lines(time,results$`predall$fit`,col="red")
#dbWriteTable(con, "test", value=results, overwrite=TRUE,row.names=TRUE)
#dbDisconnect(con)