#include <iostream>
using namespace std;

// Function to perform the CRC process (Sender + Receiver)
void crcProcess() {
    int datasize, divisorsize;

    cout << "<---- Sender Side ---->\n";
    cout << "Enter the number of data bits: ";
    cin >> datasize;

    int data[datasize];
    cout << "Enter the data bits (0s and 1s): ";
    for (int i = 0; i < datasize; i++) {
        cin >> data[i];
    }

    cout << "Enter the number of divisor bits: ";
    cin >> divisorsize;

    int divisor[divisorsize];
    cout << "Enter the divisor bits (0s and 1s): ";
    for (int i = 0; i < divisorsize; i++) {
        cin >> divisor[i];
    }

    cout << "\nData bits: ";
    for (int i = 0; i < datasize; i++)
        cout << data[i];
    cout << "\nDivisor bits: ";
    for (int i = 0; i < divisorsize; i++)
        cout << divisor[i];
    cout << endl;

    // Append (divisorsize - 1) zeros to data
    int totalsize = datasize + divisorsize - 1;
    int temp[totalsize];
    for (int i = 0; i < datasize; i++)
        temp[i] = data[i];
    for (int i = datasize; i < totalsize; i++)
        temp[i] = 0;

    cout << "\nAfter appending zeros: ";
    for (int i = 0; i < totalsize; i++)
        cout << temp[i];
    cout << endl;

    // ---------------- CRC Division ----------------
    for (int i = 0; i <= totalsize - divisorsize; i++) {  // ✅ Corrected loop boundary
        if (temp[i] == 1) {  // Perform XOR when leading bit is 1
            for (int j = 0; j < divisorsize; j++)
                temp[i + j] = temp[i + j] ^ divisor[j];
        }
    }

    // Extract remainder (CRC)
    int crc[divisorsize - 1];
    for (int i = 0; i < divisorsize - 1; i++) {
        crc[i] = temp[totalsize - (divisorsize - 1) + i];  // ✅ Corrected remainder index
    }

    cout << "CRC bits (remainder): ";
    for (int i = 0; i < divisorsize - 1; i++)
        cout << crc[i];
    cout << endl;

    // Create transmitted frame = data + CRC
    int tf[totalsize];
    for (int i = 0; i < datasize; i++)
        tf[i] = data[i];
    for (int i = 0; i < divisorsize - 1; i++)
        tf[datasize + i] = crc[i];

    cout << "Transmitted Frame: ";
    for (int i = 0; i < totalsize; i++)
        cout << tf[i];
    cout << endl;

    // ---------------- Receiver Side ----------------
    cout << "\n<---- Receiver Side ---->\n";

    int receiver[totalsize];
    cout << "Received bits: ";
    for (int i = 0; i < totalsize; i++) {
        receiver[i] = tf[i];
        cout << receiver[i];
    }
    cout << endl;

    // Perform CRC check on receiver side
    for (int i = 0; i <= totalsize - divisorsize; i++) {  // ✅ Same boundary correction
        if (receiver[i] == 1) {
            for (int j = 0; j < divisorsize; j++) {
                receiver[i + j] = receiver[i + j] ^ divisor[j];
            }
        }
    }

    // Check for any remainder bits (error detection)
    bool error = false;
    for (int i = totalsize - (divisorsize - 1); i < totalsize; i++) {
        if (receiver[i] != 0) {
            error = true;
            break;
        }
    }

    if (error)
        cout << "Error detected in received data." << endl;
    else
        cout << "No error detected. Data is correct." << endl;
}

// ---------------- Main Menu ----------------
int main() {
    int choice;
    while (true) {
        cout << "\n===== CRC ERROR DETECTION MENU =====" << endl;
        cout << "1. Perform CRC Calculation" << endl;
        cout << "2. Exit" << endl;
        cout << "Enter your choice: ";
        cin >> choice;

        switch (choice) {
            case 1:
                crcProcess();
                break;
            case 2:
                cout << "Exiting... Goodbye!" << endl;
                return 0;
            default:
                cout << "Invalid choice! Please try again." << endl;
        }
    }
}
