"""
@ Set-up instructions

In order to run this tool, the following steps are required:
1. Set the working directory to the project's root.
2. Build the project into a "bin" directory under the project's root.
3. JRE must be installed and available globally through OS shell.

The above may be altered by reconfiguring the proper variables


@ Note(s)

- After each run, the compiled test files are kept for manual debugging and inspection, but deleted at the beginning of the next execution of this tool.
"""

"""
Configurations
"""
JAVA_INTERPRETER = "java"
COMPILED_PROJECT_DIR = "bin"
COMPILED_PROJECT = "open_source.amuyal_tal.yagbc2a.Main"
TESTS_DIR = "tests"
TEST_TEMP_BIN_DIR = "bin"
TEST_EXPECTATION_DIR = "tests"


"""
Imports
"""
import os


"""
Helper functions
"""
def compile(fileName):
	compileCommand = JAVA_INTERPRETER + " -cp " + COMPILED_PROJECT_DIR + " " + COMPILED_PROJECT + " " + TESTS_DIR + "/" + fileName + ".asm -o " + TEST_TEMP_BIN_DIR + "/" + fileName + ".gb"
	os.popen(compileCommand).read()

	if os.path.isfile(TEST_TEMP_BIN_DIR + "/" + fileName + ".gb"):
		return True
	else:
		errors.append('`' + fileName + ".asm` couldn't be compiled")
		return False

def compareCompilation(fileName):
	with open(TEST_EXPECTATION_DIR + "/" + fileName + ".gb", "rb") as expected, open(TEST_TEMP_BIN_DIR + "/" + fileName + ".gb", "rb") as result:
		if expected.read() == result.read():
			return True
		else:
			errors.append("`" + file + "` produces unexpected results")
			return False


"""
Clean previous temporary compilations
"""
for file in os.listdir(TEST_TEMP_BIN_DIR):
	if file.endswith(".gb"):
		os.remove(TEST_TEMP_BIN_DIR + "/" + file)


"""
Create report variables
"""
errors = []
testCount = 0


"""
Execute test-suit
"""
for file in os.listdir(TESTS_DIR):
	if file.endswith(".asm"):
		testCount = testCount + 1
		fileName = file[:-4];

		#Each iteration has it's line
		print("Compiling `" + file + "`...", end = "", flush = True)
		if not compile(fileName):
			print(" failed")
			continue
		print(" done", end = "", flush = True)

		print(", testing...", end = "", flush = True)
		if not compareCompilation(fileName):
			print(" failed")
			continue
		print(" done", end = "", flush = True)

		print(".") #End iteration line


"""
Print visual segmentation
"""
print()
print("=" * 10 + " Test-suit run complete " + "=" * 10)
print()


"""
Prepare report information
"""
strTestCount = str(testCount)
strSuccessfulCount = str(testCount - len(errors))
strErrorsCount = str(len(errors))


"""
Report result
"""
print(strTestCount + " tests, " + strSuccessfulCount + " successful, " + strErrorsCount + " errors.")

if len(errors) > 0:
	print(strErrorsCount + " errors detected:")
	index = 0
	for error in errors:
		index = index + 1

		print()
		print(" " + str(index) + ". " + error)
