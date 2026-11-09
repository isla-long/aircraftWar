package edu.hitsz.dao;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GradeDaoImplTest {

    private GradeDaoImpl gradeDao;
    private final String  TEST_PATH = "/Users/hoshino/Documents/Codes/java/Lab1/AircraftWar-base1.0/test/edu/hitsz/dao/test_grade";
    @BeforeEach
    void setUp() {
        // 每个测试用例开始前，初始化一个新的 Dao
        gradeDao = new GradeDaoImpl(1);

        // 注意：由于你的 grades 列表在实现类里可能没初始化，
        // 我们需要通过反射或者在实现类构造函数里加上 grades = new ArrayList<>();
        // 这里假设你的实现类已经处理了初始化。
    }

    @Test
    @DisplayName("测试添加成绩及自动排序功能")
    void testAddGradeAndSort() {
        gradeDao.addGrade("101", 80, "2026-04-15");
        gradeDao.addGrade("102", 95, "2026-04-15"); // 最高分
        gradeDao.addGrade("103", 70, "2026-04-15");

        List<Map<String, Object>> all = gradeDao.getAllGrades();

        assertEquals(3, all.size());
        // 验证排序：成绩高的应该在索引 0 (95 > 80 > 70)
        assertEquals(95, all.get(0).get("Grade"));
        assertEquals(70, all.get(2).get("Grade"));
    }

    @Test
    @DisplayName("测试根据 ID 查找成绩（包含拷贝验证）")
    void testFindById() {
        gradeDao.addGrade("101", 90, "2026-04-15");

        List<Map<String, Object>> result = gradeDao.findById("101");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(90, result.get(0).get("Grade"));

        // 验证深拷贝：修改查询出的结果，不应影响原始数据
        result.get(0).put("Grade", 0);
        assertNotEquals(0, gradeDao.getAllGrades().get(0).get("Grade"), "findById 返回的应该是拷贝件");
    }

    @Test
    @DisplayName("测试删除成绩")
    void testDeleteGrade() {
        gradeDao.addGrade("101", 100, "2026-04-15");
        gradeDao.addGrade("102", 90, "2026-04-15");

        // 执行删除
        gradeDao.deleteGrade("101");

        List<Map<String, Object>> all = gradeDao.getAllGrades();
        assertEquals(1, all.size());
        assertEquals("102", all.get(0).get("ID"));
    }

    @Test
    @DisplayName("测试查询不存在的 ID")
    void testFindByIdNotFound() {
        List<Map<String, Object>> result = gradeDao.findById("999");
        assertNull(result, "找不到时应按照你的逻辑返回 null");
    }

    @Disabled
    @Test
    @DisplayName("测试：数据持久化循环（存入 -> 写入文件 -> 从文件读取）")
    void testFilePersistenceCycle() {
        // 1. 添加测试数据
        // 注意：addGrade 内部应该已经调用了 gradesSort
        gradeDao.addGrade("userA", 100, "2026-04-16 10:00:00");
        gradeDao.addGrade("userB", 80, "2026-04-16 10:05:00");

        // 2. 将数据写入文件
        gradeDao.writeGradeFile();

        // 4. 清空当前内存数据，模拟程序重启
        gradeDao.getAllGrades().clear();
        assertEquals(0, gradeDao.getAllGrades().size());

        // 5. 从文件加载回内存
        gradeDao.loadGradeFile();

        // 6. 断言验证数据一致性
        List<Map<String, Object>> loadedGrades = gradeDao.getAllGrades();
        assertEquals(2, loadedGrades.size(), "加载后的数量应为 2");
        assertEquals("userA", loadedGrades.get(0).get("ID"), "最高分应该是 userA");
        assertEquals(100, (int) loadedGrades.get(0).get("Grade"));
    }

    @Test
    @DisplayName("测试：空文件加载情况")
    void testLoadEmptyFile() {
        // 创建一个空文件
        gradeDao.getAllGrades().clear();
        gradeDao.writeGradeFile();

        // 尝试加载
        assertDoesNotThrow(() -> gradeDao.loadGradeFile(), "加载空文件不应抛出异常");
        assertEquals(0, gradeDao.getAllGrades().size());
    }
}
