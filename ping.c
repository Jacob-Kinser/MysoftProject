#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <signal.h>
#include <sys/wait.h>

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

int main(){

	 FILE * fp;
    char * line = NULL;
    size_t len = 0;
    ssize_t read;

    fp = fopen("building.txt", "r");
    if (fp == NULL)
        exit(EXIT_FAILURE);

FILE * fp2 =fopen("failed.txt", "a");
 
   

    while ((read = getline(&line, &len, fp)) != -1) {
	int i = 0;
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
        printf("%s\n", line);
	//printf("length of string: %zu \n",strlen(line));
	pid_t pid;
	pid = fork();
	if(pid != 0 ){
		wait(NULL);

		 FILE * fp1;
   		 char * line1 = NULL;
   		 size_t len1 = 0;
  		  ssize_t read1;

  		  fp1 = fopen("output.txt", "r");
  			  if (fp == NULL)
     			   exit(EXIT_FAILURE);

 		while ((read1 = getline(&line1, &len1, fp1)) != -1) {
			int j = 0;
			//printf("%s\n",line1);
			while(j< strlen(line1)){
				if(j+4 < strlen(line1)){
					if(line1[j] == '1'){
						//printf("in first if get after 1:%c%c%c\n",line1[j+1],line1[j+2],line1[j+3]);
					}
					if(line1[j] == '1' && line1[j+1] == '0' && line1[j+2] == '0'
						&& line1[j+3] == '%'){//&& line1[j+4] == ' ' && line1[j+5] == 'p'
						//&& line[j+6] == 'a' && line1[j+7] == 'c' && line1[j+8] == 'k'
						//&& line[j+9] == 'e' && line1[j+10] == 't' && line1[j+11] == ' '
						//&& line1[j+12] == 'l' && line1[j+13] == 'o' && line1[j+14] == 's'
						//&& line1[j+15] == 's'){
						printf("packet loss\n");
						 fprintf(fp2, "%s\n", line);
					}
				}
				j++;
			}
		}
		remove("output.txt");
	}
	if (pid < 0){ //fork failed
		printf("fork failed\n");
			
	}
	else if (pid == 0) {// child process
		int file = open("output.txt", O_WRONLY | O_CREAT, 0777);
		if(file == -1) printf("File could not be created\n");
		int file2 = dup2(file,1);

		execlp("ping","ping","-c 3", line,NULL);
	} 
	printf("\n\n\n\n\n");
    }
    fclose(fp);
    if (line)
    free(line);
    exit(EXIT_SUCCESS);

}