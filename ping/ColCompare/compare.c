#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <signal.h>
#include <sys/wait.h>
#include <ctype.h>

char* deblank(char* input)                                         
{
    int i,j;
    char *output=input;
    for (i = 0, j = 0; i<strlen(input); i++,j++)          
    {
        if (input[i]!=' ')                           
            output[j]=input[i];                     
        else
            j--;                                     
    }
    output[j]=0;
    return output;
}

int main(void){
    int checked = 0;
	 FILE * fp;
    char * line = NULL;
    size_t len = 0;
    ssize_t read;

    fp = fopen("building.txt", "r");
    if (fp == NULL)
        exit(EXIT_FAILURE);

    FILE * fpDIG;
    char * lineDIG = NULL;
    size_t lenDIG = 0;
    ssize_t readDIG;

    fpDIG = fopen("new_ap_name.txt", "r");
    if (fpDIG == NULL)
        exit(EXIT_FAILURE);

    FILE * fp2 =fopen("failedBulkStr.txt", "a");
 
   

    while ((read = getline(&line, &len, fp)) != -1 &&  (readDIG= getline(&lineDIG, &lenDIG, fpDIG)) != -1) {
    checked++;
	char tele[] = ".tele.iastate.edu";
	char * fullFQDN;
	int len = len =strlen(lineDIG);
	if(lineDIG[strlen(lineDIG) - 1] == '\n'){
		len =strlen(lineDIG) - 2;
	}

	if((fullFQDN = malloc(len+strlen(tele))) != NULL){
		int fill = 0;
		fullFQDN[0] = '\0'; 
		while(fill < len){

			fullFQDN[fill] = lineDIG[fill];
			fill++;
		}
		int fill2 = 0;
		while(fill2 < strlen(tele)){
			fullFQDN[fill] = tele[fill2];
			fill++;
			fill2++;
		}
			fullFQDN[fill] = 0;
            //printf("%s\n",fullFQDN);
	} else {
		printf("malloc failed!\n");
		return 1;
	}

  //  printf("%s\n", line);
    int findName = 0;
    int newLen = 0;
    while(findName < strlen(line)){
        if(line[findName] == 'a' && line[findName + 1] == 'p' && line[findName + 2] == '-'){
           // printf("found\n");
            break;
        }
        findName++;
    }
    newLen = strlen(line) - findName;
    if(line[strlen(line) - 1] == '\n'){
        newLen = newLen - 2;
        //printf("trim one more");
    }
    char BulkStr[newLen];
    int i = 0;
    while(i < newLen){
        if(line[findName] != '\n'){
           // printf("line: %d %c\n",i,line[findName]);
            BulkStr[i] = line[findName];
        }
        i++; findName++;
    }
    BulkStr[i] = 0; 
    
   // printf("%s\n\n",BulkStr);

    i = 0;
    int true = 0;
    if(strlen(BulkStr) != strlen(fullFQDN)){
        true = 1;
       // printf("size%d %d\n",strlen(BulkStr) , strlen(fullFQDN));
    }
    else{
        while(i < strlen(BulkStr)){
            if(tolower(BulkStr[i]) != tolower(fullFQDN[i])){
                //printf("%c %c\n ",BulkStr[i] , fullFQDN[i] );
                true = 1;
            }
            i++;
        }
    }
    if(true == 1){
        printf("at line: %d\n",checked);
        printf("Bulk: %s\n",fullFQDN);
        printf("newA: %s\n\n",BulkStr);
        fprintf(fp2, "%s\n", line);
    }
    }
   printf("checked %d\n",checked);

}