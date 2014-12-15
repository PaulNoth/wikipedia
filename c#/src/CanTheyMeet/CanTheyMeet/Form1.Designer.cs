namespace CanTheyMeet
{
    partial class CanTheyMeet
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(CanTheyMeet));
            this.comboboxPerson2 = new System.Windows.Forms.ComboBox();
            this.comboboxPerson1 = new System.Windows.Forms.ComboBox();
            this.LabelPerson2 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.button1 = new System.Windows.Forms.Button();
            this.result = new System.Windows.Forms.Label();
            this.searchPerson1 = new System.Windows.Forms.Button();
            this.searchPerson2 = new System.Windows.Forms.Button();
            this.lblNameP1 = new System.Windows.Forms.Label();
            this.lblBirthDateP1 = new System.Windows.Forms.Label();
            this.lblDeathDateP1 = new System.Windows.Forms.Label();
            this.lblDeathDateP2 = new System.Windows.Forms.Label();
            this.lblBirthDateP2 = new System.Windows.Forms.Label();
            this.lblNameP2 = new System.Windows.Forms.Label();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.groupBox1.SuspendLayout();
            this.SuspendLayout();
            // 
            // comboboxPerson2
            // 
            this.comboboxPerson2.FormattingEnabled = true;
            this.comboboxPerson2.Location = new System.Drawing.Point(404, 42);
            this.comboboxPerson2.Name = "comboboxPerson2";
            this.comboboxPerson2.Size = new System.Drawing.Size(300, 21);
            this.comboboxPerson2.TabIndex = 0;
            this.comboboxPerson2.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.comboboxPerson2_KeyPress_1);
            // 
            // comboboxPerson1
            // 
            this.comboboxPerson1.FormattingEnabled = true;
            this.comboboxPerson1.Location = new System.Drawing.Point(12, 41);
            this.comboboxPerson1.Name = "comboboxPerson1";
            this.comboboxPerson1.Size = new System.Drawing.Size(300, 21);
            this.comboboxPerson1.TabIndex = 1;
            this.comboboxPerson1.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.searchPerson1_Click);
            // 
            // LabelPerson2
            // 
            this.LabelPerson2.AutoSize = true;
            this.LabelPerson2.Location = new System.Drawing.Point(401, 26);
            this.LabelPerson2.Name = "LabelPerson2";
            this.LabelPerson2.Size = new System.Drawing.Size(56, 13);
            this.LabelPerson2.TabIndex = 2;
            this.LabelPerson2.Text = "Person #2";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(12, 15);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(56, 13);
            this.label1.TabIndex = 3;
            this.label1.Text = "Person #1";
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(628, 194);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(158, 62);
            this.button1.TabIndex = 4;
            this.button1.Text = "CanTheyMeet";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // result
            // 
            this.result.AutoSize = true;
            this.result.Font = new System.Drawing.Font("Impact", 48F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.result.Location = new System.Drawing.Point(6, 15);
            this.result.Name = "result";
            this.result.Size = new System.Drawing.Size(207, 80);
            this.result.TabIndex = 5;
            this.result.Text = "Result";
            // 
            // searchPerson1
            // 
            this.searchPerson1.Location = new System.Drawing.Point(323, 40);
            this.searchPerson1.Name = "searchPerson1";
            this.searchPerson1.Size = new System.Drawing.Size(75, 23);
            this.searchPerson1.TabIndex = 6;
            this.searchPerson1.Text = "Search";
            this.searchPerson1.UseVisualStyleBackColor = true;
            this.searchPerson1.Click += new System.EventHandler(this.searchPerson1_Click);
            // 
            // searchPerson2
            // 
            this.searchPerson2.Location = new System.Drawing.Point(711, 41);
            this.searchPerson2.Name = "searchPerson2";
            this.searchPerson2.Size = new System.Drawing.Size(75, 23);
            this.searchPerson2.TabIndex = 7;
            this.searchPerson2.Text = "Search";
            this.searchPerson2.UseVisualStyleBackColor = true;
            this.searchPerson2.Click += new System.EventHandler(this.searchPerson2_Click);
            // 
            // lblNameP1
            // 
            this.lblNameP1.AutoSize = true;
            this.lblNameP1.Location = new System.Drawing.Point(9, 84);
            this.lblNameP1.Name = "lblNameP1";
            this.lblNameP1.Size = new System.Drawing.Size(102, 13);
            this.lblNameP1.TabIndex = 8;
            this.lblNameP1.Text = "Name of Person #1:";
            // 
            // lblBirthDateP1
            // 
            this.lblBirthDateP1.AutoSize = true;
            this.lblBirthDateP1.Location = new System.Drawing.Point(9, 115);
            this.lblBirthDateP1.Name = "lblBirthDateP1";
            this.lblBirthDateP1.Size = new System.Drawing.Size(119, 13);
            this.lblBirthDateP1.TabIndex = 9;
            this.lblBirthDateP1.Text = "Birth date of Person #1:";
            // 
            // lblDeathDateP1
            // 
            this.lblDeathDateP1.AutoSize = true;
            this.lblDeathDateP1.Location = new System.Drawing.Point(9, 145);
            this.lblDeathDateP1.Name = "lblDeathDateP1";
            this.lblDeathDateP1.Size = new System.Drawing.Size(127, 13);
            this.lblDeathDateP1.TabIndex = 10;
            this.lblDeathDateP1.Text = "Death date of Person #1:";
            // 
            // lblDeathDateP2
            // 
            this.lblDeathDateP2.AutoSize = true;
            this.lblDeathDateP2.Location = new System.Drawing.Point(401, 145);
            this.lblDeathDateP2.Name = "lblDeathDateP2";
            this.lblDeathDateP2.Size = new System.Drawing.Size(127, 13);
            this.lblDeathDateP2.TabIndex = 13;
            this.lblDeathDateP2.Text = "Death date of Person #2:";
            // 
            // lblBirthDateP2
            // 
            this.lblBirthDateP2.AutoSize = true;
            this.lblBirthDateP2.Location = new System.Drawing.Point(401, 115);
            this.lblBirthDateP2.Name = "lblBirthDateP2";
            this.lblBirthDateP2.Size = new System.Drawing.Size(119, 13);
            this.lblBirthDateP2.TabIndex = 12;
            this.lblBirthDateP2.Text = "Birth date of Person #2:";
            // 
            // lblNameP2
            // 
            this.lblNameP2.AutoSize = true;
            this.lblNameP2.Location = new System.Drawing.Point(401, 84);
            this.lblNameP2.Name = "lblNameP2";
            this.lblNameP2.Size = new System.Drawing.Size(102, 13);
            this.lblNameP2.TabIndex = 11;
            this.lblNameP2.Text = "Name of Person #2:";
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.result);
            this.groupBox1.Location = new System.Drawing.Point(12, 161);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(610, 99);
            this.groupBox1.TabIndex = 14;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Result";
            // 
            // CanTheyMeet
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(794, 261);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.lblDeathDateP2);
            this.Controls.Add(this.lblBirthDateP2);
            this.Controls.Add(this.lblNameP2);
            this.Controls.Add(this.lblDeathDateP1);
            this.Controls.Add(this.lblBirthDateP1);
            this.Controls.Add(this.lblNameP1);
            this.Controls.Add(this.searchPerson2);
            this.Controls.Add(this.searchPerson1);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.LabelPerson2);
            this.Controls.Add(this.comboboxPerson1);
            this.Controls.Add(this.comboboxPerson2);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "CanTheyMeet";
            this.Text = "CanTheyMeet";
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ComboBox comboboxPerson2;
        private System.Windows.Forms.ComboBox comboboxPerson1;
        private System.Windows.Forms.Label LabelPerson2;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Label result;
        private System.Windows.Forms.Button searchPerson1;
        private System.Windows.Forms.Button searchPerson2;
        private System.Windows.Forms.Label lblNameP1;
        private System.Windows.Forms.Label lblBirthDateP1;
        private System.Windows.Forms.Label lblDeathDateP1;
        private System.Windows.Forms.Label lblDeathDateP2;
        private System.Windows.Forms.Label lblBirthDateP2;
        private System.Windows.Forms.Label lblNameP2;
        private System.Windows.Forms.GroupBox groupBox1;
    }
}

