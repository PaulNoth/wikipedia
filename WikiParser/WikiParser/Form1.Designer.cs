namespace WikiParser
{
    partial class Wiki
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
            this.openFileDialog1 = new System.Windows.Forms.OpenFileDialog();
            this.label1 = new System.Windows.Forms.Label();
            this.textBox1 = new System.Windows.Forms.TextBox();
            this.BtnBrowseInput = new System.Windows.Forms.Button();
            this.BtnParse = new System.Windows.Forms.Button();
            this.BtnExport = new System.Windows.Forms.Button();
            this.LbDisambPages = new System.Windows.Forms.ListBox();
            this.LbPages = new System.Windows.Forms.ListBox();
            this.RtbPagesInfo = new System.Windows.Forms.RichTextBox();
            this.RtbProgramInfo = new System.Windows.Forms.RichTextBox();
            this.saveFileDialog1 = new System.Windows.Forms.SaveFileDialog();
            this.SuspendLayout();
            // 
            // openFileDialog1
            // 
            this.openFileDialog1.FileName = "openFileDialog1";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(12, 16);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(31, 13);
            this.label1.TabIndex = 0;
            this.label1.Text = "Input";
            // 
            // textBox1
            // 
            this.textBox1.Location = new System.Drawing.Point(55, 13);
            this.textBox1.Name = "textBox1";
            this.textBox1.Size = new System.Drawing.Size(147, 20);
            this.textBox1.TabIndex = 2;
            // 
            // BtnBrowseInput
            // 
            this.BtnBrowseInput.Location = new System.Drawing.Point(208, 11);
            this.BtnBrowseInput.Name = "BtnBrowseInput";
            this.BtnBrowseInput.Size = new System.Drawing.Size(75, 23);
            this.BtnBrowseInput.TabIndex = 4;
            this.BtnBrowseInput.Text = "Browse";
            this.BtnBrowseInput.UseVisualStyleBackColor = true;
            this.BtnBrowseInput.Click += new System.EventHandler(this.BtnBrowseInput_Click);
            // 
            // BtnParse
            // 
            this.BtnParse.Location = new System.Drawing.Point(15, 39);
            this.BtnParse.Name = "BtnParse";
            this.BtnParse.Size = new System.Drawing.Size(124, 23);
            this.BtnParse.TabIndex = 6;
            this.BtnParse.Text = "Parse";
            this.BtnParse.UseVisualStyleBackColor = true;
            this.BtnParse.Click += new System.EventHandler(this.BtnParse_Click);
            // 
            // BtnExport
            // 
            this.BtnExport.Location = new System.Drawing.Point(145, 39);
            this.BtnExport.Name = "BtnExport";
            this.BtnExport.Size = new System.Drawing.Size(137, 23);
            this.BtnExport.TabIndex = 7;
            this.BtnExport.Text = "Export";
            this.BtnExport.UseVisualStyleBackColor = true;
            this.BtnExport.Click += new System.EventHandler(this.BtnExport_Click);
            // 
            // LbDisambPages
            // 
            this.LbDisambPages.FormattingEnabled = true;
            this.LbDisambPages.Location = new System.Drawing.Point(12, 67);
            this.LbDisambPages.Name = "LbDisambPages";
            this.LbDisambPages.Size = new System.Drawing.Size(143, 355);
            this.LbDisambPages.TabIndex = 8;
            this.LbDisambPages.SelectedIndexChanged += new System.EventHandler(this.LbDisambPages_SelectedIndexChanged);
            // 
            // LbPages
            // 
            this.LbPages.FormattingEnabled = true;
            this.LbPages.Location = new System.Drawing.Point(162, 67);
            this.LbPages.Name = "LbPages";
            this.LbPages.Size = new System.Drawing.Size(120, 355);
            this.LbPages.TabIndex = 9;
            this.LbPages.SelectedIndexChanged += new System.EventHandler(this.LbPages_SelectedIndexChanged);
            // 
            // RtbPagesInfo
            // 
            this.RtbPagesInfo.Location = new System.Drawing.Point(289, 106);
            this.RtbPagesInfo.Name = "RtbPagesInfo";
            this.RtbPagesInfo.Size = new System.Drawing.Size(455, 316);
            this.RtbPagesInfo.TabIndex = 10;
            this.RtbPagesInfo.Text = "";
            // 
            // RtbProgramInfo
            // 
            this.RtbProgramInfo.Location = new System.Drawing.Point(289, 4);
            this.RtbProgramInfo.Name = "RtbProgramInfo";
            this.RtbProgramInfo.Size = new System.Drawing.Size(455, 96);
            this.RtbProgramInfo.TabIndex = 11;
            this.RtbProgramInfo.Text = "";
            // 
            // Wiki
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(756, 429);
            this.Controls.Add(this.RtbProgramInfo);
            this.Controls.Add(this.RtbPagesInfo);
            this.Controls.Add(this.LbPages);
            this.Controls.Add(this.LbDisambPages);
            this.Controls.Add(this.BtnExport);
            this.Controls.Add(this.BtnParse);
            this.Controls.Add(this.BtnBrowseInput);
            this.Controls.Add(this.textBox1);
            this.Controls.Add(this.label1);
            this.Name = "Wiki";
            this.Text = "WikiParser";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.OpenFileDialog openFileDialog1;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox textBox1;
        private System.Windows.Forms.Button BtnBrowseInput;
        private System.Windows.Forms.Button BtnParse;
        private System.Windows.Forms.Button BtnExport;
        private System.Windows.Forms.ListBox LbDisambPages;
        private System.Windows.Forms.ListBox LbPages;
        private System.Windows.Forms.RichTextBox RtbPagesInfo;
        private System.Windows.Forms.RichTextBox RtbProgramInfo;
        private System.Windows.Forms.SaveFileDialog saveFileDialog1;
    }
}

