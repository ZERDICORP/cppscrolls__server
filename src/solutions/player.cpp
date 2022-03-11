void script(int* arr, int size)
{ 
 while (true) {} for (int i = 0; i < size; ++i)
    for (int j = 0; j < size; ++j)
      if (arr[i] < arr[j])
      {   
        int temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
      }   
}

#include <cassert>
#include <vector>
  
void test()
{ 
  int size = 3;
  int test[size] { 3, 1, 2 };
  
  script(test, size);
  
  assert(std::vector<int>(test, test + 3) == std::vector<int>({1, 2, 3}));
}

int main()
{
	test();

	return 0;
}