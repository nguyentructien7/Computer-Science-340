/*
 
 * Name:Kelsey Nguyen
 * Class: CS340
 * Description: User select either InsertionSort, MergeSort, or HeapSort and file size which is 30k,60k,120k, or 
    150k and file type which is permuted or sorted. Then plot all four on 5 sorted and 5 permuted lists.
 * Due Date: August 31, 2017

 */

/* 
 * File:   main.c
 * Author:tnguyen
 *
 * Created on August 25, 2017, 1:15 PM
 */


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <time.h>
#include <limits.h>

/*
 * 
 */



//asks user to pick either insertion, merge, or heap sort
char selectSort() {
    int legalOption;
    char menuOption;

    legalOption = 0;
    while (legalOption == 0) {
        printf("How would you like to sort the file?   I for Insertion,   M for Merge ,     H for Heap \n");
        scanf("   %c", &menuOption);
        if ((menuOption == 'I') || (menuOption == 'M') || (menuOption == 'H'))
            legalOption = 1;
        else
            printf("\007 Invalid choice. Please try again.\n");
    }
    return menuOption;

}
//------------------------------------------------------------------------------------------------------------------------------------------------
//asks user to pick the size of the file---> 30k,60k,120k, or 150k
int selectSize() {
    int legalOption;
    int menuOption;

    legalOption = 0;
    while (legalOption == 0) {
        printf("What file size would you like?    30000   60000   90000   120000   150000\n");
        scanf("   %d", &menuOption);
        if ((menuOption == 30000) || (menuOption == 60000) || (menuOption == 90000) || (menuOption == 120000) || (menuOption == 150000))
            legalOption = 1;
        else
            printf("\007 Invalid choice. Please try again.\n");
    }

    return menuOption;

}
//------------------------------------------------------------------------------------------------------------------------------------------------
//asks user to pick either permuted or sorted file
char selectType() {
    int legalOption;
    char menuOption;

    legalOption = 0;
    while (legalOption == 0) {
        printf("What type of file would you like?    P for Permuted,     S for Sorted\n");
        scanf("   %c", &menuOption);
        if ((menuOption == 'P') || (menuOption == 'S'))
            legalOption = 1;
        else
            printf("\007 Invalid choice. Please try again.\n");
    }

    return menuOption;

}

//------------------------------------------------------------------------------------------------------------------------------------------------
//Open the file and does insertion sort
void insertionSort(int size, char *name) {

    int actualSize = size * 1000;
    char *phrases[actualSize];
    FILE *openFile = fopen(name, "r");
    FILE *fileOutput = fopen("output.txt", "w");
    int i, j;

    for (i = 0; i < actualSize; ++i) {
        phrases[i] = malloc(256);
        fscanf(openFile, "%s", phrases[i]);
    }
    for (j = 1; j < actualSize; j++) {
        const char *key;
        key = phrases[j];
        for (i = j - 1; i >= 0; i--) {
            if (strcasecmp(phrases[i], key) < 0) {
                break;
            }
            phrases[i + 1] = phrases[i];
        }
        phrases[i + 1] = key;
    }
   

    for (i = 0; i < actualSize; i++) {
        fprintf(fileOutput, "%s\n", phrases[i]);
    }

    for (i = 0; i < actualSize; ++i) {
        phrases[i] = NULL;
        free(phrases[i]);
    }
    fclose(fileOutput);
    fclose(openFile);
}

//------------------------------------------------------------------------------------------------------------------------------------------------
//Merging the Array Function
void Merge(char* arrary[], int low, int mid, int high) 
{
    char* sorted[high - low + 1];
    for (int i = 0; i <= high - low; i++) {
        sorted[i] = malloc(256);
    }
    int leftPtr = low;
    int rightPtr = mid + 1;
    int sortPtr = 0;
    
    
    //sort the array
    while (leftPtr <= mid || rightPtr <= high) {
        if (leftPtr > mid) {
            strcpy(sorted[sortPtr], arrary[rightPtr]);
            rightPtr++;
        } else if (rightPtr > high) {
            strcpy(sorted[sortPtr], arrary[leftPtr]);
            leftPtr++;
        } else if (strcmp(arrary[leftPtr], arrary[rightPtr]) < 0) {
            strcpy(sorted[sortPtr], arrary[leftPtr]);
            leftPtr++;
        } else {
            strcpy(sorted[sortPtr], arrary[rightPtr]);
            rightPtr++;
        }
        sortPtr++;
    }
    for (int i = 0; i < high - low + 1; i++) {
        strcpy(arrary[low + i], sorted[i]);
    }

}
//------------------------------------------------------------------------------------------------------------------------------------------------
//Main MergeSort function
void MergeSort(char *box[], int low, int high) 
{
    int mid;
    if (low < high) {
        mid = (low + high) / 2;
        MergeSort(box, low, mid);
        MergeSort(box, mid + 1, high);
        Merge(box, low, mid, high);
    }
}

//------------------------------------------------------------------------------------------------------------------------------------------------
//Open the file, calls mergeSort, then outputs to file
void partition(int size, char *title) {

    int actualSize = size * 1000;
    char *phrases[actualSize];
    FILE *openFile = fopen(title, "r");
    FILE *fileOutput = fopen("output.txt", "w");
    int i;

    //scans from file into array
    for (i = 0; i < actualSize; ++i) {
        phrases[i] = malloc(256);
        fscanf(openFile, "%s", phrases[i]);
    }

    /*---------------------------------- call merge sort */
    MergeSort(phrases, 0, actualSize - 1);

    

    fclose(openFile);

    //prints array to output file
    for (i = 0; i < actualSize; i++) {
        fprintf(fileOutput, "%s\n", phrases[i]);
    }

    for (i = 0; i < actualSize; ++i) {
        free(phrases[i]);
    }
    fclose(fileOutput);
}

//------------------------------------------------------------------------------------------------------------------------------------------------
//Given a tree that is a heap except for the root
void maxHeapify(char *a[], int value, int heapsize) {
    int *temp, largest;
    int left = (2 * value) + 1;
    int right = (2 * value) + 2;

    if ((left <= heapsize) && (strcmp(a[left], a[value]) > 0)) {
        largest = left;
    } else {
        largest = value;
    }

    if ((right <= heapsize) && (strcmp(a[right], a[largest]) > 0)) {
        largest = right;
    }

    if (largest != value) {
        temp = malloc(256);
        strcpy(temp, a[value]);
        strcpy(a[value], a[largest]);
        strcpy(a[largest], temp);
        maxHeapify(a, largest, heapsize);
    }

}

//------------------------------------------------------------------------------------------------------------------------------------------------
// using the maxHeapfy() to to construct a heap starting with the last node
void buildMaxHeap(char *arr[], int heapsize) {

    for (int i = heapsize / 2; i >= 0; i--) {
        maxHeapify(arr, i, heapsize);
    }
}

//------------------------------------------------------------------------------------------------------------------------------------------------
//Open the file, calls buildMaxHeap then maxHeapify, then outputs to file
void heapSort(int size, char *title) {

    int actualSize = size * 1000;
    char *temp = malloc(256);
    int heapSize = actualSize - 1;
    char *array[actualSize];
    FILE *openFile = fopen(title, "r");
    FILE *fileOutput = fopen("output.txt", "w");
    

    //scans file & puts in array
    for (int i = 0; i < actualSize; ++i) {
        array[i] = malloc(256);
        fscanf(openFile, "%s", array[i]);
    }
    /*----------------------------------------------------------start of heap sort */

    buildMaxHeap(array, heapSize);

    for (int i = heapSize; i > 0; i--) {
        strcpy(temp, array[i]);
        strcpy(array[i], array[0]);
        strcpy(array[0], temp);
        heapSize--;
        maxHeapify(array, 0, heapSize);
    }
    /*----------------------------------------------------------end of heap sort */

    //prints array to output file
    for (int i = 0; i < actualSize; i++) {
        fprintf(fileOutput, "%s\n", array[i]);
    }
    

    fclose(fileOutput);
    fclose(openFile);
}

//------------------------------------------------------------------------------------------------------------------------------------------------
int main(int argc, char** argv) {

    //obtain user selection
    int size = selectSize();
    char type = selectType();
    char sort = selectSort();
    double timeSpent;
    clock_t begin = clock();
    
    

    //calls the methods based on the user selection
    switch (size) {
        case 30:
            if (type == 'P') {
                if (sort == 'I') {
                    insertionSort(size, "perm30k.txt");
                } else if (sort == 'M') {
                    partition(size, "perm30k.txt");
                } else if (sort == 'H') {
                    heapSort(size, "perm30k.txt");
                } else
                    printf("Invalid entry");
            } else if (type == 'S') {
                if (sort == 'I') {
                    insertionSort(size, "sorted30k.txt");
                } else if (sort == 'M') {
                    partition(size, "sorted30k.txt");
                } else if (sort == 'H') {
                    heapSort(size, "sorted30k.txt");
                } else
                    printf("Invalid entry");
            }
            break;
            
           
        case 60:
            if (type == 'P') {
                if (sort == 'I') {
                    insertionSort(size, "perm60k.txt");
                } else if (sort == 'M') {
                    partition(size, "perm60k.txt");
                } else if (sort == 'H') {
                    heapSort(size, "perm60k.txt");
                } else
                    printf("Invalid entry");
            } else if (type == 'S') {
                if (sort == 'I') {
                    insertionSort(size, "sorted60k.txt");
                } else if (sort == 'M') {
                    partition(size, "sorted60k.txt");
                } else if (sort == 'H') {
                    heapSort(size, "sorted60k.txt");
                } else
                    printf("Invalid entry");
            }
            break;
            
            
        case 90:
            if (type == 'P') {
                if (sort == 'I') {
                    insertionSort(size, "perm90k.txt");
                } else if (sort == 'M') {
                    partition(size, "perm90k.txt");
                } else if (sort == 'H') {
                    heapSort(size, "perm90k.txt");
                } else
                    printf("Invalid entry");
            } else if (type == 'S') {
                if (sort == 'I') {
                    insertionSort(size, "sorted90k.txt");
                } else if (sort == 'M') {
                    partition(size, "sorted90k.txt");
                } else if (sort == 'H') {
                    heapSort(size, "sorted90k.txt");
                } else
                    printf("Invalid entry");
            }
            break;
            
            
        case 120:
            if (type == 'P') {
                if (sort == 'I') {
                    insertionSort(size, "perm120k.txt");
                } else if (sort == 'M') {
                    partition(size, "perm120k.txt");
                } else if (sort == 'H') {
                    heapSort(size, "perm120k.txt");
                } else
                    printf("Invalid entry");
            } else if (type == 'S') {
                if (sort == 'I') {
                    insertionSort(size, "sorted120k.txt");
                } else if (sort == 'M') {
                    partition(size, "sorted120k.txt");
                } else if (sort == 'H') {
                    heapSort(size, "sorted120k.txt");
                } else
                    printf("Invalid entry");
            }
            break;
            
            
        case 150:
            if (type == 'P') {
                if (sort == 'I') {
                    insertionSort(size, "perm150k.txt");
                } else if (sort == 'M') {
                    partition(size, "perm150k.txt");
                } else if (sort == 'H') {
                    heapSort(size, "perm150k.txt");
                } else
                    printf("Invalid entry");
            } else if (type == 'S') {
                if (sort == 'I') {
                    insertionSort(size, "sorted150k.txt");
                } else if (sort == 'M') {
                    partition(size, "sorted150k.txt");
                } else if (sort == 'H') {
                    heapSort(size, "sorted150k.txt");
                } else
                    printf("Invalid entry");
            }
            break;
    }
    char path [10000];
    realpath ("../../", path);
    
    //gets the execution time 
    clock_t end = clock();
    timeSpent = (double) (end - begin) / CLOCKS_PER_SEC;
    printf("\nLocation of file: %s ", path);
    printf("\nExecution Time in ms: %f", timeSpent);


    return (EXIT_SUCCESS);
}