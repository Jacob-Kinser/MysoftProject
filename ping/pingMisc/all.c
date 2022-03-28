#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <signal.h>
#include <sys/wait.h>


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
    char tele[] = ".misc.iastate.edu.";
	char * fullFQDN;
	//printf("%s\n",lineDIG);
	// lineDIG[ strcspn(lineDIG, "\n" ) ] = '\0';
	int len = len =strlen(line);
	if(line[strlen(line) - 1] == '\n'){
		len =strlen(line) - 2;
	}
	
	//printf("%s\n",lineDIG);

	if((fullFQDN = malloc(len+strlen(tele))) != NULL){
		//printf("%d\n",len+strlen(tele)+1);
		int fill = 0;
		fullFQDN[0] = '\0'; 
		while(fill < len){
			//printf("%c\n",lineDIG[fill]);
			fullFQDN[fill] = line[fill];
			fill++;
		}
		int fill2 = 0;
		while(fill2 < strlen(tele)){
		//	printf("%c\n",tele[fill2]);
			fullFQDN[fill] = tele[fill2];
			fill++;
			fill2++;
		}
			fullFQDN[fill] = 0;
		//printf("%d\n",fill);
		// fullFQDN[0] = '\0';  
		// strcat(fullFQDN,lineDIG);
		printf("%s\n",fullFQDN);
		// //fullFQDN[ strcspn( fullFQDN, "\n" ) ] = 0;
		// strcat(fullFQDN,tele);
		//printf("%s\n",fullFQDN);
	} else {
		printf("malloc failed!\n");
		return 1;
	}

        //printf("Retrieved line of length %zu:\n", read);
        //printf("%s\n", line);
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
					if(line1[j] == '0' && line1[j+1] == '%' && line1[j+2] == ' '
						&& line1[j+3] == 'p'){//&& line1[j+4] == ' ' && line1[j+5] == 'p'
						//&& line[j+6] == 'a' && line1[j+7] == 'c' && line1[j+8] == 'k'
						//&& line[j+9] == 'e' && line1[j+10] == 't' && line1[j+11] == ' '
						//&& line1[j+12] == 'l' && line1[j+13] == 'o' && line1[j+14] == 's'
						//&& line1[j+15] == 's'){
						printf("pingable\n");
						 fprintf(fp2, "%s\n", fullFQDN);
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

		execlp("ping","ping","-c 3", fullFQDN,NULL);
	} 
	printf("\n\n\n\n\n");
    }
    fclose(fp);
    if (line)
    free(line);
    exit(EXIT_SUCCESS);

}