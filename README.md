# guava-demo
[toc]

##### 基本工具

1. **使用和避免null:**

   对于null值使用场景基本就是表明某种缺失的情形，可能是已经有一个默认的值，或没有值，或找不到值。举个例子，比如说要记录异常考试所有学生的成绩，如果用`int`类型的话，是无法区分考0分的同学和缺考的同学的，如果使用`Integer`的话，成绩为0的就为考0分的同学，而`null`值的同学就为缺考的同学，此时的null值就具有一定的业务意义。

   还有一个例子，`map.get("key");` 的到的值是`null`，或者一次数据库查询操作得到的对象为`null`，我们理所当然认为这个值是不存在的，然后再看下面的代码：

   ```java
   Map<Object, Object> maps = new HashMap<Object, Object>();
   maps.put("leon", null);
   
   System.out.println(maps.get(null));    // null
   System.out.println(maps.get("leon")); //  null
   ```

   可以看出，`java`中的集合是接受`null`作为值的，当你此时的业务需要使用`null`作为列表的值例如要构造一个稀疏矩阵，那么此时，`map` 中对应某个键的值是 `null`，和 `map `中没有对应某个键的值，是非常容易混淆的两种情况。

   那么为什么要避免`null`呢？   与其说避免`null`不如说避免**`NullPointerException`**。

   ###### guava中的null值处理

   1. **Optional**

      创建 Optional 实例（以下都是静态方法）：

      | 方法                       | 描述                                                   |
      | -------------------------- | ------------------------------------------------------ |
      | `Optional.absent()`        | 创建引用缺失的 Optional 实例                           |
      | `Optional.fromNullable(T)` | 创建指定引用的 Optional 实例，若引用为 null 则表示缺失 |
      | `Optional.of(T)`           | 创建指定引用的 Optional 实例，若引用为 null 则快速失败 |

      用 Optional 实例查询引用（以下都是非静态方法）：

      | 方法                  | 描述                                                         |
      | --------------------- | ------------------------------------------------------------ |
      | `T get()`             | 返回 Optional 所包含的引用，若引用缺失，则抛出 java.lang.IllegalStateException |
      | `T or(T)`             | 返回 Optional 所包含的引用，若引用缺失，返回指定的值         |
      | `T orNull()`          | 返回 Optional 所包含的引用，若引用缺失，返回 null            |
      | `Set asSet()`         | 返回 Optional 所包含引用的单例不可变集，如果引用存在，返回一个只有单一元素的集合，如果引用缺失，返回一个空集合。 |
      | `boolean isPresent()` | 如果 Optional 包含非 null 的引用（引用存在），返回true       |

   2. **其他处理null的方法**

      `Objects.firstNonNull(T, T)` 方法可以使用一个默认值来替换`null` ，但如果两个值都为`null`的话会抛出**NPE**， 替代方案： `Optional.of(first).or(second)` 。

      `Strings.emptyToNull(String)`   `Strings.nullToEmpty(String)`    `Strings.isNullOrEmpty(String)`

   ###### java8中的null值处理

   1. **Optional**

      Optional在Java8的实现中基本具有了guava Optional的所有功能，并且提供了例如：

      ```java
      public Optional<T> filter(Predicate<? super T> predicate) 
      public<U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper)
      ```

      的方法，其中有一个坑需要注意！！！

      方法：

      ```java
      /**
       * @throws NullPointerException if value is not present and {@code other}is
       * null
       */
      public T orElseGet(Supplier<? extends T> other) {
         return value != null ? value : other.get();
      }
      ```

      尽量避免使用此方法，因为会抛出`NPE`。

   以上涉及到的测试用例在：`com.leon.guava.demo.basicutil.UseNull`

   

2. 前置条件(请勿在项目中使用):

   `java`提供了`assert`断言，`guava`提供了`precondition`前置条件，从字面意思看断言是必须要成立，前置条件是满足才能往下，`assert`只在`debug`环境生效，而`precondition`任何时候都生效。其常用`api`如下：

   | **方法声明（不包括额外参数）**                       | **描述**                                                     | **检查失败时抛出的异常**  |
   | ---------------------------------------------------- | ------------------------------------------------------------ | ------------------------- |
   | `checkArgument(boolean)`                             | 检查 boolean 是否为 true，用来检查传递给方法的参数。         | IllegalArgumentException  |
   | `checkNotNull(T)`                                    | 检查 value 是否为 null，该方法直接返回 value，因此可以内嵌使用 checkNotNull`。` | NullPointerException      |
   | `checkState(boolean)`                                | 用来检查对象的某些状态。                                     | IllegalStateException     |
   | `checkElementIndex(int index, int size)`             | 检查 index 作为索引值对某个列表、字符串或数组是否有效。index>=0 && index<size * | IndexOutOfBoundsException |
   | `checkPositionIndex(int index, int size)`            | 检查 index 作为位置值对某个列表、字符串或数组是否有效。index>=0 && index<=size * | IndexOutOfBoundsException |
   | `checkPositionIndexes(int start, int end, int size)` | 检查[start, end]表示的位置范围对某个列表、字符串或数组是否有效* | IndexOutOfBoundsException |

   为什么不推荐在项目中使用呢，因为检查失败后会抛出异常，这个异常是不强制捕获的，若未显式捕获，异常会一直往上层抛，并且异常的堆栈不友好，难以定位问题。

   以上涉及的测试用例在：`com.leon.guava.demo.basicutil.Precondition`

   

3. **排序**
`guava` 提供了`ordering`排序器，从实现上看，`ordering`实例就是一个特殊的`comparator`实例，它将很多基于`Comparator`的静态方法包装成自己的实例方法，并且提供了链式调用方法来钉子和增强现有的比较器。

**创建排序器**：常见的排序器可以由下面的静态方法创建

| **方法**           | **描述**                                               |
| ------------------ | ------------------------------------------------------ |
| `natural()`        | 对可排序类型做自然排序，如数字按大小，日期按先后排序   |
| `usingToString()`  | 按对象的字符串形式做字典排序[lexicographical ordering] |
| `from(Comparator)` | 把给定的 Comparator 转化为排序器                       |

```java
Ordering<String> ordering = new Ordering<String>() {
	@Override
    public int compare(@Nullable String left, @Nullable String right) {
    	return Ints.compare(left.length(), right.length());
    }
};
//1. 使用from
 User user1 = new User("leon", 15);
 User user2 = new User("chenliang", 18);
 List<User> users = new ArrayList<User>();
 users.add(user1);
 users.add(user2);
 //构造排序器
 Ordering<User> ordering = Ordering.from((left, right) ->          Ints.compare(left.age, right.age));
 //获取排序结果
 List<User> usersSorted = ordering.immutableSortedCopy(users);
//2. 使用natural
int[] arr = {7, 3, 5, 2, 8};
Ordering<Integer> ordering = Ordering.natural();
List<Integer> list = ordering.immutableSortedCopy( Arrays.stream(arr).boxed().collect(Collectors.toList()));
//3. 使用usingToString
List<String> list = Lists.newArrayList("leon", "chenliang");
Ordering ording = Ordering.usingToString();
List<String> listSorted = ording.sortedCopy(list);
```

**链式调用方法**：通过链式调用，可以由给定的排序器衍生出其它排序器

| **方法**               | **描述**                                                     |
| ---------------------- | ------------------------------------------------------------ |
| `reverse()`            | 获取语义相反的排序器                                         |
| `nullsFirst()`         | 使用当前排序器，但额外把 null 值排到最前面。                 |
| `nullsLast()`          | 使用当前排序器，但额外把 null 值排到最后面。                 |
| `compound(Comparator)` | 合成另一个比较器，以处理当前排序器中的相等情况。             |
| `lexicographical()`    | 基于处理类型 T 的排序器，返回该类型的可迭代对象 Iterable<T>的排序器。 |
| `onResultOf(Function)` | 对集合中元素调用 Function，再按返回值用当前排序器排序。      |

```java
Ordering<User> ordering = Ordering.natural().nullsFirst().onResultOf(user -> {
	return user.age;
});
```

**运用排序器**：Guava 的排序器实现有若干操纵集合或元素值的方法

| **方法**                               | **描述**                                                     | **另请参见**                                      |
| -------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------- |
| `greatestOf(Iterable iterable, int k)` | 获取可迭代对象中最大的k个元素。                              | `leastOf`                                         |
| `isOrdered(Iterable)`                  | 判断可迭代对象是否已按排序器排序：允许有排序值相等的元素。   | `isStrictlyOrdered`                               |
| `sortedCopy(Iterable)`                 | 判断可迭代对象是否已严格按排序器排序：不允许排序值相等的元素。 | `immutableSortedCopy`                             |
| `min(E, E)`                            | 返回两个参数中最小的那个。如果相等，则返回第一个参数。       | `max(E, E)`                                       |
| `min(E, E, E, E...)`                   | 返回多个参数中最小的那个。如果有超过一个参数都最小，则返回第一个最小的参数。 | `max(E, E, E, E...)`                              |
| `min(Iterable)`                        | 返回迭代器中最小的元素。如果可迭代对象中没有元素，则抛出 NoSuchElementException。 | `max(Iterable)`, `min(Iterator)`, `max(Iterator)` |

```java
 List<UserInfo> orderStepInfo = Ordering
               .<UserInfo>from((left, right) -> Longs.compare(right.getTimestamp(),                     left.getTimestamp()))
                .immutableSortedCopy(Lists.transform(userInfos, it -> {
                    Order order = new Order();
                    order.setUserName(it.getUserName());
                    order.setTimestamp(it.getTimestamp());
                    return order;
                }));
```

**java8使用stream排序**

```java
//1. 按自然序拍序
List<User> users = userList.stream().sorted().collect(Collectors.toList());
//2. 按某一字段信息排序
List<User> users = userList.stream().sorted(Comparator.comparing(User::getAge)).collect(Collectors.toList());
//3. 按某一字段倒序
List<User> users = userList.stream().sorted(Comparator.comparing(User::getAge).reversed()).collect(Collectors.toList());
```

以上涉及的测试用例在：`com.leon.guava.demo.basicutil.Sorts`

##### 集合



##### 缓存

##### 函数式编程

##### 并发

##### 字符串处理

##### 原生类型

##### 区间

##### I/O

##### 散列

##### 事件总线

##### 数学运算

##### 反射

##### guava-retry

