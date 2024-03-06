package src;

import java.util.Scanner;

public class Simpletron {
    // registers;
    private int accumulador;
    private int instructionCounter;
    private int operationCode;
    private int operand;
    private int instructionRegister;
    private final int[] memory;

    public Simpletron(){
        memory = new int[100];
        accumulador = 0;
        instructionCounter = 0;
        operationCode = 0;
        operand = 0;
        instructionRegister = 0;
    }

    public void writeCode() {
        var sc = new Scanner(System.in);
        System.out.println(
            "*** Welcome to Simpletron! ***\n" + //
            "*** Please enter your program one instruction\n" + //
            "***\n" + //
            "*** (or data word) at a time. I will display\n" + //
            "***\n" + //
            "*** the location number and a question mark (?). ***\n" + //
            "*** You then type the word for that location.\n" + //
            "***\n" + //
            "*** Type -99999 to stop entering your program.\n" + //
            "***\n" //
        );
        for (int index = 0; index < memory.length; index++) {
            System.out.printf("%02d ? ", index);
            var input = sc.nextLine();
            var inputInt = validateCommand(input);

            if (inputInt == -99999) {
                System.out.println("** Program writing completed ***");
                sc.close();
                return;
            } else {
                memory[index] = inputInt;
            }
        }
        System.out.println("** Program writing completed ***");
        sc.close();
    }

    public void insertCode(String[] instructions){
        if (instructions.length > 100){
            System.out.println("**** Program too long ***");
            System.out.println("*** Simpletron execution abnormally terminated ***");
            System.exit(-1);
        }

        for (int i = 0; i < instructions.length; i++) {
            int commandValue = validateCommand(instructions[i]);
            if (commandValue == -99999) {
                System.out.println("** Program writing completed ***");
                return;
            }

            memory[i] = Integer.parseInt(instructions[i]);
        }
    }

    public void exec(){
        System.out.println("*** Program loading completed ***");
        System.out.println("*** Program execution begins ***");

        while (true){
            instructionRegister = memory[instructionCounter];
            operationCode = instructionRegister / 100;
            operand = instructionRegister % 100;

            switch (operationCode){
                case 10:
                    readValue();
                    break;
                case 11:
                    System.out.println(memory[operand]);
                    break;
                case 20:
                    accumulador = memory[operand];
                    break;
                case 21:
                    memory[operand] = accumulador;
                    break;
                case 30:
                    accumulador += memory[operand];
                    checkAccumulatorOverflow();
                    break;
                case 31:
                    accumulador -= memory[operand];
                    checkAccumulatorOverflow();
                    break;
                case 32:
                    checkDivisionByZero();
                    accumulador /= memory[operand];
                    break;
                case 33:
                    accumulador *= memory[operand];
                    checkAccumulatorOverflow();
                    break;
                case 40:
                    instructionCounter = operand;
                    continue;
                case 41:
                    if (accumulador < 0) instructionCounter = operand;
                    continue;
                case 42:
                    if (accumulador == 0) instructionCounter = operand;
                    continue;
                case 43:
                    endExecution();
                    break;
                default:
                    InvalidCommand();
            }
            instructionCounter++;
        }        
    }

    private int validateCommand(String command){
        if (!command.matches("\\d+")){
            InvalidCommand();
        }
        int commandValue = Integer.parseInt(command);
        if (commandValue < -9999 || commandValue > 9999){
            InvalidCommand();
        }
        return commandValue;
    }

    private void readValue(){
        var sc = new Scanner(System.in);
        System.out.print(": ");
        String instruction = sc.nextLine();
        sc.close();
        if (instruction.matches("\\d+")){
            InvalidCommand();
        }
        memory[operand] = Integer.parseInt(instruction);
    }

    private void displayDump(int accumulador, int instructionCounter, int instructionRegister, int operationCode, int operand){
        System.out.println("REGISTERS:");
        System.out.printf("accumulator: %04d\n", accumulador);
        System.out.printf("instructionCounter: %02d\n", instructionCounter);
        System.out.printf("instructionRegister: %04d\n", instructionRegister);
        System.out.printf("operationCode: %02d\n", operationCode);
        System.out.printf("operand: %02d\n", operand);
        System.out.println("MEMORY:");
        // for (int i = 0; i < memory.length; i++) {
        //     System.out.printf("%02d: %04d\n", i, memory[i]);
        // }
    }

    private void checkAccumulatorOverflow(){
        if (accumulador < -9999 || accumulador > 9999){
            System.out.println("*** Accumulator overflow ***");
            System.out.println("*** Simpletron execution abnormally terminated ***");
            displayDump(accumulador, instructionCounter, instructionRegister, operationCode, operand);
            System.exit(-1);
        }
    }

    private void checkDivisionByZero(){
        if (accumulador == 0 && operand == 0) {
            System.out.println("**** Attempt to divide by zero ***");
            System.out.println("*** Simpletron execution abnormally terminated ***");
            displayDump(accumulador, instructionCounter, instructionRegister, operationCode, operand);
            System.exit(-1);
        }
    }

    private void InvalidCommand(){
        System.out.println("**** Invalid Command ***");
        System.out.println("*** Simpletron execution abnormally terminated ***");
        displayDump(accumulador, instructionCounter, instructionRegister, operationCode, operand);
        System.exit(-1);
    }

    private void endExecution(){
        System.out.println("*** Simpletron execution terminated ***");
        // displayDump(accumulador, instructionCounter, instructionRegister, operationCode, operand);
        System.exit(0);
    }
}