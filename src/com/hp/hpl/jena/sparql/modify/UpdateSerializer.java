/*
 * (c) Copyright 2007, 2008 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sparql.modify;


import java.util.Iterator;

import com.hp.hpl.jena.sparql.modify.op.*;
import com.hp.hpl.jena.sparql.serializer.FmtTemplateARQ;
import com.hp.hpl.jena.sparql.serializer.FormatterARQ;
import com.hp.hpl.jena.sparql.serializer.SerializationContext;
import com.hp.hpl.jena.sparql.syntax.Template;
import com.hp.hpl.jena.sparql.util.FmtUtils;
import com.hp.hpl.jena.sparql.util.IndentedWriter;

public class UpdateSerializer implements UpdateVisitor
{
    // Newline policy: assume multiline output and finish each section with an NL 
    
    IndentedWriter out ;
    SerializationContext sCxt ;
    
    public UpdateSerializer(IndentedWriter out, SerializationContext sCxt)
    { this.out = out ; this.sCxt = sCxt ; }
    
    private void visitModifyHeader(String name, String word, UpdateModifyBase modify)
    {
        out.print(name) ;
        for ( Iterator iter = modify.getGraphNames().iterator() ; iter.hasNext() ; )
        {
            String iri = (String)iter.next() ;
            if ( word != null )
            {
                out.print(" ") ;
                out.print(word) ;
            }
            out.print(" ") ;
            out.print(FmtUtils.stringForURI(iri, sCxt)) ;
        }
        out.println(); 
    }
    
    private void visitModifyTrailer(UpdateModifyBase modify)
    {
        if ( modify.getElement() != null )
        {
            out.println("WHERE") ;
            out.incIndent() ;
            FormatterARQ.format(out, sCxt,modify.getElement()) ;
            out.decIndent() ;
            out.println();
        }
    }
    
    public void visit(UpdateModify modify)
    { 
        visitModifyHeader("MODIFY", "GRAPH", modify) ;
        out.println("DELETE") ;
        printTemplate(modify.getDeleteTemplate()) ;
        out.println("INSERT") ;
        printTemplate(modify.getInsertTemplate()) ;
        visitModifyTrailer(modify) ;
    }        

    public void visit(UpdateDelete delete)
    { 
        visitModifyHeader("DELETE", "FROM", delete) ;
        printTemplate(delete.getDeleteTemplate()) ;
        visitModifyTrailer(delete) ;
    }
    
    public void visit(UpdateInsert insert)
    { 
        visitModifyHeader("INSERT", "INTO", insert) ;
        printTemplate(insert.getInsertTemplate()) ;
        visitModifyTrailer(insert) ;
    }

    public void visit(UpdateClear clear)
    {
        out.print("CLEAR") ;
        if ( clear.hasGraphName() )
        {
            out.print(" ") ;
            FmtUtils.stringForNode(clear.getGraphName(), sCxt) ;
        }
        out.println() ;
    }

    public void visit(UpdateLoad load)
    {
        out.print("LOAD") ;
        for ( Iterator iter = load.getLoadIRIs().iterator() ; iter.hasNext() ; )
        {
            String iri = (String)iter.next() ;
            out.print(" ") ;
            FmtUtils.stringForURI(iri, sCxt) ;
        }
        
        if ( load.hasGraphName() )
        {
            out.print(" ") ;
            out.print("INTO") ;
            out.print(" ") ;
            String s = FmtUtils.stringForNode(load.getGraphName(), sCxt) ; 
            out.print(s) ;
        }
        out.println() ;
    }

    public void visit(UpdateDrop drop)
    {
        out.print("DROP") ;
        if ( drop.isSilent() )
        {
            out.print(" ") ;
            out.print("SILENT") ;
        }
        
        out.print(" ") ;
        out.print("GRAPH") ;
        out.print(" ") ;
        out.print(FmtUtils.stringForNode(drop.getIRI(), sCxt)) ;
        out.println() ;
    }

    public void visit(UpdateCreate create)
    {
        out.print("CREATE") ;
        if ( create.isSilent() )
        {
            out.print(" ") ;
            out.print("SILENT") ;
        }
        
        out.print(" ") ;
        out.print("GRAPH") ;
        out.print(" ") ;
        out.print(FmtUtils.stringForNode(create.getIRI(), sCxt)) ;
        out.println() ;
    }
    
    private void printTemplate(Template template)
    {
        out.incIndent() ;
        FmtTemplateARQ.format(out, sCxt, template) ;
        out.decIndent() ;
    }
}

/*
 * (c) Copyright 2007, 2008 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */