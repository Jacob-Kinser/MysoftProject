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

	 FILE * fp;
    char * line = NULL;
    size_t len = 0;
    ssize_t read;

    fp = fopen("buildingDIG.txt", "r");
    if (fp == NULL)
        exit(EXIT_FAILURE);

    FILE * fpDIG;
    char * lineDIG = NULL;
    size_t lenDIG = 0;
    ssize_t readDIG;

    fpDIG = fopen("buildingDIGFQDN.txt", "r");
    if (fpDIG == NULL)
        exit(EXIT_FAILURE);


    FILE * fp2 =fopen("failedDIG.txt", "a");
 
   int checked = 0;

    while ((read = getline(&line, &len, fp)) != -1 &&  (readDIG= getline(&lineDIG, &lenDIG, fpDIG)) != -1) {
	checked++;
	int findName = 0;
    int newLen = 0;
    while(findName < strlen(lineDIG)){
        if(lineDIG[findName] == 'a' && lineDIG[findName + 1] == 'p' && lineDIG[findName + 2] == '-'){
           // printf("found\n");
            break;
        }
        findName++;
    }
    newLen = strlen(lineDIG) - findName;
    if(lineDIG[strlen(lineDIG) - 1] == '\n'){
        newLen = newLen - 2;
        //printf("trim one more");
    }
    char BulkStr[newLen];
    int i = 0;
    while(i < newLen){
        if(lineDIG[findName] != '\n'){
          //  printf("line: %d %c\n",i,lineDIG[findName]);
            BulkStr[i] = lineDIG[findName];
        }
        i++; findName++;
    }
	BulkStr[i] = '.'; 
	BulkStr[i+1] = 0; 
	//printf("len %d %d %c\n",newLen,strlen(BulkStr),BulkStr[strlen(BulkStr) - 1]);
	i = 0;
	int dot = 0;
	while(i < strlen(line)){

		if(line[i] == '.'){
			dot++;
		}
		else if(dot == 3 && (line[i] != '1' && line[i] != '2' &&line[i] != '3' &&
				line[i] != '4' &&line[i] != '5' &&line[i] != '6' &&
				line[i] != '7' &&line[i] != '8' &&line[i] != '9' &&
				line[i] != '0' )){
			line[i] = '\0';

		}
		i++;
	}
        //printf("Retrieved line of length %zu:\n", read);
	//printf("length of string: %zu \n",strlen(line));
	//printf("IP: %s\n", line);
	pid_t pid;
	pid = fork();
	if(pid != 0 ){
		wait(NULL);

		 FILE * fp1;
   		 char * line1 = NULL;
   		 size_t len1 = 0;
  		  ssize_t read1;

			

  		  fp1 = fopen("outputDIG.txt", "r");
  			  if (fp == NULL)
     			   exit(EXIT_FAILURE);

 		while ((read1 = getline(&line1, &len1, fp1)) != -1) {
			int j = 0;
            int true = 0;
			if(strlen(line1) - 1 != strlen(BulkStr)){
				true = 1;
			}
			else{
				while(j< strlen(BulkStr)){
					if(tolower(line1[j]) != tolower(BulkStr[j])){
						true = 1;
					}
					j++;
				}
			}
            if(true == 1){
				printf("IP: %s\n", line);
			printf("dig result: %s\n",line1);
            printf("FQDN from file: %s\n\n\n",BulkStr);	
                fprintf(fp2, "%s\n", line);
            }
		}
		remove("outputDIG.txt");
	}
	if (pid < 0){ //fork failed
		printf("fork failed\n");
			
	}
	else if (pid == 0) {// child process
		int file = open("outputDIG.txt", O_WRONLY | O_CREAT, 0777);
		if(file == -1) printf("File could not be created\n");
		int file2 = dup2(file,1);

		execlp("dig","dig","-x", line ,"+short",NULL);
	} 
	//free(BulkStr);
	free(lineDIG);
    }
	printf("lines checked: %d\n", checked);
    fclose(fp);
    if (line)
    free(line);
    exit(EXIT_SUCCESS);


}